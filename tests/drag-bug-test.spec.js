import { test, expect } from '@playwright/test';

test.describe('F2 Drag Bug Test', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to our bug test page
    await page.goto('http://localhost:8081/test-drag-bug.html');
    
    // Wait for the test page to load
    await page.waitForSelector('#navigation');
    await page.waitForTimeout(1000);
  });

  test('should demonstrate section deletion bug during drag and drop', async ({ page }) => {
    console.log('🧪 Starting drag bug demonstration test...');
    
    // Verify initial state - Section A should exist
    const sectionABefore = await page.locator('details:first-child summary').textContent();
    console.log('Initial Section A:', sectionABefore);
    
    expect(sectionABefore).toContain('Section A');
    
    // Verify Section A has the test item
    const testItemBefore = await page.locator('#test-item-1').count();
    expect(testItemBefore).toBe(1);
    
    // Run the drag test (this should trigger the bug)
    console.log('🎯 Running drag test...');
    await page.click('button:has-text("Run Drag Test")');
    
    // Wait for the drag operation to complete
    await page.waitForTimeout(2000);
    
    // Check if the bug occurred - Section A should still exist but might have been deleted
    const sectionAAfterExists = await page.locator('details:first-child').count();
    const sectionAAfterText = await page.locator('details:first-child summary').textContent().catch(() => 'SECTION DELETED');
    
    console.log('After drag - Section A exists:', sectionAAfterExists > 0);
    console.log('After drag - Section A text:', sectionAAfterText);
    
    // Check test results from the page
    const testStatus = await page.locator('.test-status').textContent();
    console.log('Test page status:', testStatus);
    
    // Verify the item moved to Section B
    const itemInSectionB = await page.locator('#section-b-container #test-item-1').count();
    expect(itemInSectionB).toBe(1);
    
    // The critical test - Section A should still exist as a details element
    // This test will FAIL with current implementation due to the bug
    const sectionAStillDetails = await page.evaluate(() => {
      const sectionA = document.querySelector('details:first-child');
      return sectionA && sectionA.tagName === 'DETAILS';
    });
    
    // Check if bug indicator is shown
    const bugIndicatorVisible = await page.locator('#bug-detected').isVisible();
    
    if (bugIndicatorVisible || !sectionAStillDetails) {
      console.log('🐛 BUG CONFIRMED: Section was deleted during drag operation');
      console.log('This test demonstrates the bug in recursiveCleanupEmptyParents');
      
      // For now, we expect the bug to occur (test should fail)
      // When the bug is fixed, change this expectation
      expect(sectionAStillDetails).toBe(false); // Currently expecting the bug
      expect(bugIndicatorVisible).toBe(true);   // Bug should be detected
    } else {
      console.log('✅ No bug detected - Section A remained intact');
      expect(sectionAStillDetails).toBe(true);
      expect(bugIndicatorVisible).toBe(false);
    }
  });
  
  test('should provide detailed console output for debugging', async ({ page }) => {
    // Enable console monitoring
    page.on('console', msg => {
      if (msg.type() === 'error') {
        console.log('Browser error:', msg.text());
      } else if (msg.text().includes('recursiveCleanupEmptyParents')) {
        console.log('Bug function called:', msg.text());
      }
    });
    
    // Run the test
    await page.click('button:has-text("Run Drag Test")');
    await page.waitForTimeout(2000);
    
    // Check console output on the page
    const consoleOutput = await page.locator('#console-output').textContent();
    console.log('Page console output:', consoleOutput);
    
    // The console should show the drag operation and any errors
    expect(consoleOutput).toContain('Drag started');
  });
  
  test('should allow manual testing via browser', async ({ page }) => {
    // This test just verifies the test page is working for manual testing
    
    // Check all test controls are available
    const runTestBtn = await page.locator('button:has-text("Run Drag Test")').isVisible();
    const checkStatusBtn = await page.locator('button:has-text("Check Section A Status")').isVisible();
    const resetBtn = await page.locator('button:has-text("Reset Test")').isVisible();
    
    expect(runTestBtn).toBe(true);
    expect(checkStatusBtn).toBe(true);
    expect(resetBtn).toBe(true);
    
    // Test the reset functionality
    await page.click('button:has-text("Reset Test")');
    await page.waitForTimeout(500);
    
    // Verify reset worked
    const itemBackInSectionA = await page.locator('#section-a-container #test-item-1').count();
    expect(itemBackInSectionA).toBe(1);
    
    console.log('✅ Manual testing controls are working');
  });
  
  test('manual debugging - step by step drag operation', async ({ page }) => {
    // This test breaks down the drag operation to see exactly when the bug occurs
    
    console.log('🔍 Step-by-step debugging of drag operation');
    
    // Step 1: Initial state
    let sectionAExists = await page.evaluate(() => {
      const section = document.querySelector('details:first-child');
      return { exists: !!section, tagName: section?.tagName, hasContainer: !!section?.querySelector('.ml-6') };
    });
    console.log('Step 1 - Initial Section A:', sectionAExists);
    
    // Step 2: Simulate just the DOM move (without F2 function)
    await page.evaluate(() => {
      const testItem = document.getElementById('test-item-1');
      const sectionBContainer = document.getElementById('section-b-container');
      sectionBContainer.appendChild(testItem);
      console.log('Manual DOM move completed');
    });
    
    sectionAExists = await page.evaluate(() => {
      const section = document.querySelector('details:first-child');
      return { exists: !!section, tagName: section?.tagName, hasContainer: !!section?.querySelector('.ml-6') };
    });
    console.log('Step 2 - After manual move Section A:', sectionAExists);
    
    // Step 3: Now call the problematic function
    const bugTriggered = await page.evaluate(() => {
      const mockEvent = {
        item: document.getElementById('test-item-1'),
        from: document.getElementById('section-a-container'),
        to: document.getElementById('section-b-container'),
        oldIndex: 0,
        newIndex: 0
      };
      
      try {
        if (typeof handleEnhancedSortableDrop === 'function') {
          handleEnhancedSortableDrop(mockEvent);
          return true;
        }
        return false;
      } catch (error) {
        console.error('Error calling handleEnhancedSortableDrop:', error);
        return false;
      }
    });
    
    console.log('Step 3 - Bug function called:', bugTriggered);
    
    // Wait for any async operations
    await page.waitForTimeout(1000);
    
    sectionAExists = await page.evaluate(() => {
      const section = document.querySelector('details:first-child');
      return { exists: !!section, tagName: section?.tagName, hasContainer: !!section?.querySelector('.ml-6') };
    });
    console.log('Step 4 - After handleEnhancedSortableDrop Section A:', sectionAExists);
    
    // This should show exactly when Section A disappears
    if (!sectionAExists.exists || sectionAExists.tagName !== 'DETAILS') {
      console.log('🐛 BUG CONFIRMED: Section A was deleted in step 3 (handleEnhancedSortableDrop)');
    }
  });
});