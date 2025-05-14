class TabsComponent extends HTMLElement {
    constructor() {
        super();
        // Internal state
        this._activeTab = 0;
        console.log('gb-tabs')
    }
    connectedCallback() {
        // Add styles to document if not already present
        if (!document.getElementById('gb-tabs-styles')) {
            const styleSheet = document.createElement('style');
            styleSheet.id = 'gb-tabs-styles';
            styleSheet.textContent = `
          gb-tabs {
            display: block;
            font-family: inherit;
          }
          .tabs-container {
            display: flex;
            flex-direction: column;
          }
          .tabs-bar {
            display: flex;
            border-bottom: 1px solid #ccc;
          }
          .tab {
            padding: 0.5rem 1rem;
            cursor: pointer;
            background-color: #f5f5f5;
            border: 1px solid #ccc;
            border-bottom: none;
            margin-right: 5px;
            border-radius: 4px 4px 0 0;
            user-select: none;
            font-size: 0.8rem;
          }
          .tab.active {
            background-color: white;
            position: relative;
          }
          .tab.active::after {
            content: '';
            position: absolute;
            bottom: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            background-color: white;
          }
          .content-container {
            position: relative;
            border: 1px solid #ccc;
            border-top: none;
            padding: 0.5rem;
          }
          .tab-content {
            display: none;
          }
          .tab-content.active {
            display: block;
          }
          gb-tabs > div {
            display: none;
          }
        `;
            document.head.appendChild(styleSheet);
        }

        // Render the initial component structure
        this.render();
        
        // Process tabs immediately
        this.processTabs();

        // Watch for mutations to handle dynamically added tabs
        const observer = new MutationObserver(() => this.processTabs());
        observer.observe(this, { childList: true, subtree: true, attributes: true });
    }
    
    render() {
        this.innerHTML = `
        <div class="tabs-container">
          <div class="tabs-bar" id="tabs-bar"></div>
          <div class="content-container">
            <div id="content-area"></div>
          </div>
        </div>
      `;
    }
    
    processTabs() {
        // Get all child divs with title attributes that are direct children
        const tabs = Array.from(this.querySelectorAll(':scope > div[title]'));
        
        if (tabs.length === 0) return;
        
        // Generate tabs in the tab bar
        const tabsBar = this.querySelector('#tabs-bar');
        tabsBar.innerHTML = '';
        
        // Generate content containers
        const contentArea = this.querySelector('#content-area');
        contentArea.innerHTML = '';
        
        tabs.forEach((tab, index) => {
            // Create tab button
            const tabButton = document.createElement('div');
            tabButton.className = 'tab';
            tabButton.textContent = tab.getAttribute('title');
            tabButton.dataset.index = index;
            tabButton.addEventListener('click', () => this.activateTab(index));
            tabsBar.appendChild(tabButton);
            
            // Create content container
            const contentDiv = document.createElement('div');
            contentDiv.className = 'tab-content';
            contentDiv.dataset.index = index;
            
            // Move the content instead of cloning
            contentDiv.appendChild(tab);
            
            contentArea.appendChild(contentDiv);
        });
        
        // Activate the first tab by default
        this.activateTab(0);
    }
    
    activateTab(index) {
        // Update active tab index
        this._activeTab = index;
        
        // Update tab button states
        const tabButtons = this.querySelectorAll('.tab');
        tabButtons.forEach((button, i) => {
            if (i === index) {
                button.classList.add('active');
            } else {
                button.classList.remove('active');
            }
        });
        // Update content visibility
        const contentDivs = this.querySelectorAll('.tab-content');
        contentDivs.forEach((div, i) => {
            if (i === index) {
                div.classList.add('active');
            } else {
                div.classList.remove('active');
            }
        });
        // Dispatch an event
        this.dispatchEvent(new CustomEvent('tabchange', {
            detail: { index, title: tabButtons[index]?.textContent }
        }));
    }
}
customElements.define('gb-tabs', TabsComponent);
