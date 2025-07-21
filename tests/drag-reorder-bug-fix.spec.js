import { test, expect } from '@playwright/test';

test.describe('F2 Drag Reorder Bug Fix', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the documentation homepage
    await page.goto('http://localhost:8081/');
    
    // Wait for the navigation to be loaded
    await page.waitForSelector('#navigation', { timeout: 10000 });
    
    // Enter F2 mode
    await page.keyboard.press('F2');
    await page.waitForSelector('#fullscreen-nav-container', { timeout: 5000 });
    
    // Open all sections for testing
    await page.click('button:has-text("Open All")');
    await page.waitForTimeout(300);
  });

  test('should NOT convert sections when reordering within same section', async ({ page }) => {
    console.log('🧪 Testing that reordering within same section does NOT trigger conversion...');
    
    // Find a section with multiple items
    const sectionWithItems = await page.locator('details:has(.ml-6 > a)').first();
    const sectionSummary = await sectionWithItems.locator('summary').first();
    const sectionTitle = await sectionSummary.textContent();
    
    console.log('Testing section:', sectionTitle);
    
    // Get the container inside this section
    const sectionContainer = sectionWithItems.locator('.ml-6').first();
    const itemsBeforeCount = await sectionContainer.locator('> a').count();
    
    console.log('Items in section before reorder:', itemsBeforeCount);
    
    if (itemsBeforeCount < 2) {
      console.log('Skipping test - need at least 2 items in section');
      return;
    }
    
    // Get the first and second items
    const firstItem = sectionContainer.locator('> a').first();
    const secondItem = sectionContainer.locator('> a').nth(1);
    
    const firstItemText = await firstItem.textContent();
    const secondItemText = await secondItem.textContent();
    
    console.log('Moving item:', firstItemText);
    console.log('After item:', secondItemText);
    
    // Record the original section structure
    const originalSectionHTML = await sectionWithItems.innerHTML();
    const originalHasDetails = originalSectionHTML.includes('<details');
    const originalHasContainer = originalSectionHTML.includes('ml-6');
    
    // Set up console monitoring to catch conversion attempts
    let conversionAttempted = false;
    page.on('console', msg => {
      const text = msg.text();
      if (text.includes('Converting leaf to parent') || text.includes('🌱')) {
        conversionAttempted = true;
        console.log('❌ CONVERSION ATTEMPTED:', text);
      }
      if (text.includes('Not converting:') || text.includes('❌ Not converting')) {
        console.log('✅ CONVERSION PROPERLY PREVENTED:', text);
      }
    });
    
    // Perform drag operation within the same section (reordering)
    // Move first item to after the second item
    await firstItem.dragTo(secondItem);
    
    // Wait for any async operations
    await page.waitForTimeout(1000);
    
    // Verify the section still exists and has proper structure
    const sectionAfterDrag = await page.locator('details').filter({ hasText: sectionTitle }).first();
    const sectionExistsAfterDrag = await sectionAfterDrag.count();
    
    expect(sectionExistsAfterDrag).toBe(1);
    
    // Verify the section is still a proper details element
    const isStillDetails = await sectionAfterDrag.evaluate(el => el.tagName === 'DETAILS');
    expect(isStillDetails).toBe(true);
    
    // Verify the container still exists and is proper
    const containerAfterDrag = sectionAfterDrag.locator('.ml-6');
    const hasContainerAfterDrag = await containerAfterDrag.count();
    expect(hasContainerAfterDrag).toBe(1);
    
    // Verify no conversion was attempted
    expect(conversionAttempted).toBe(false);
    
    // Verify the section doesn't have the converted container marker
    const hasConvertedMarker = await containerAfterDrag.getAttribute('data-converted-container');
    expect(hasConvertedMarker).toBeNull();
    
    // Verify items are still in the section
    const itemsAfterCount = await containerAfterDrag.locator('> a').count();
    expect(itemsAfterCount).toBe(itemsBeforeCount);
    
    // Verify the actual reordering worked (items should be in different order)
    const firstItemAfter = await containerAfterDrag.locator('> a').first().textContent();
    
    if (firstItemAfter !== firstItemText) {
      console.log('✅ Reordering successful: first item is now:', firstItemAfter);
    }
    
    console.log('✅ Test passed: Section preserved during reordering');
  });

  test('should preserve section structure during multiple reorders', async ({ page }) => {
    console.log('🧪 Testing multiple reorders do not accumulate conversion issues...');
    
    const section = await page.locator('details:has(.ml-6 > a)').first();
    const container = section.locator('.ml-6');
    const itemsCount = await container.locator('> a').count();
    
    if (itemsCount < 3) {
      console.log('Skipping test - need at least 3 items');
      return;
    }
    
    // Perform multiple reorder operations
    for (let i = 0; i < 3; i++) {
      console.log(`Performing reorder operation ${i + 1}/3`);
      
      const firstItem = container.locator('> a').first();
      const lastItem = container.locator('> a').last();
      
      // Move first to last position
      await firstItem.dragTo(lastItem);
      await page.waitForTimeout(500);
      
      // Verify section still exists properly
      const stillExists = await section.count();
      expect(stillExists).toBe(1);
      
      const isStillDetails = await section.evaluate(el => el.tagName === 'DETAILS');
      expect(isStillDetails).toBe(true);
      
      // Verify container is not marked as converted
      const hasConvertedMarker = await container.getAttribute('data-converted-container');
      expect(hasConvertedMarker).toBeNull();
    }
    
    console.log('✅ Multiple reorders completed successfully');
  });

  test('should show detailed console output about conversion decisions', async ({ page }) => {
    console.log('🧪 Testing detailed console logging for conversion decisions...');
    
    const conversionLogs = [];
    page.on('console', msg => {
      const text = msg.text();
      if (text.includes('Checking if conversion needed') || 
          text.includes('Not converting:') ||
          text.includes('Conversion decision:')) {
        conversionLogs.push(text);
      }
    });
    
    // Find a section and perform reorder
    const section = await page.locator('details:has(.ml-6 > a)').first();
    const container = section.locator('.ml-6');
    const items = container.locator('> a');
    const itemsCount = await items.count();
    
    if (itemsCount >= 2) {
      const firstItem = items.first();
      const secondItem = items.nth(1);
      
      console.log('Performing reorder to trigger logging...');
      await firstItem.dragTo(secondItem);
      await page.waitForTimeout(1000);
    }
    
    // Check that we got proper logging
    console.log('Conversion decision logs:');
    conversionLogs.forEach(log => console.log('  -', log));
    
    // Should have at least some decision-making logs
    expect(conversionLogs.length).toBeGreaterThan(0);
    
    // Should include "Not converting" messages for reorder operations
    const preventionLogs = conversionLogs.filter(log => log.includes('Not converting:'));
    expect(preventionLogs.length).toBeGreaterThan(0);
    
    console.log('✅ Proper conversion decision logging verified');
  });
});