(ns gitbok.ui.landing-hero
  (:require
   [gitbok.http :as http]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.tags :as tags]
   [gitbok.ui.heroicons :as ico]))

 ;; Common card styles
(def card-base-styles
  "Base styles for landing page cards"
  "block rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-all duration-300")

(def card-hover-styles
  "Hover styles for landing page cards"
  "hover:bg-white hover:bg-none hover:border-outline-hover")

(def card-clickable-styles
  "Combined styles for clickable cards"
  (str card-base-styles " " card-hover-styles " cursor-pointer"))

(defn getting-started-card
  "Getting Started card with tutorial icons"
  [context]
  [:div {:class "bg-gradient-to-b from-white to-surface-alt border border-outline relative rounded-lg p-6 shadow-[0px_6px_13px_0px_#00000008,0px_24px_24px_0px_#00000005,0px_54px_32px_0px_#00000003]"}
   [:div {:class "flex flex-col gap-4"}
    [:div {:class "flex flex-col"}
     [:h2 {:class "text-xl leading-8 m-0 text-on-surface-strong font-medium"} "Get your Aidbox up and running"]
     [:p {:class "text-on-surface-secondary text-sm leading-[22.75px] m-0 mt-1 font-normal"} "Get started in just a few minutes."]]

    ;; One-liner command with dark background and COPY button - proper height
    [:div {:class "bg-black rounded-lg px-[17px] py-[9px] h-[42px] flex items-center justify-between gap-4"}
     [:div {:class "font-code font-normal text-sm leading-[22.75px] tracking-[-0.02em] text-white flex-1 overflow-x-auto flex items-center whitespace-nowrap"}
      [:span {:class "text-gray-300 mr-2 select-none flex-shrink-0"} "$"]
      [:span {:class "text-white"} "curl -JO https://aidbox.app/runme && docker compose up"]]
     [:button {:class "bg-brand text-white px-2.5 py-0.5 rounded text-xs font-medium shadow-[inset_0px_-2px_1.4px_0px_#00000026] hover:bg-brand-hover transition-colors whitespace-nowrap cursor-pointer"
               :id "copy-button"
               :onclick "const btn = this; const originalText = btn.textContent; btn.textContent = 'COPIED'; navigator.clipboard.writeText('curl -JO https://aidbox.app/runme && docker compose up'); setTimeout(() => { btn.textContent = originalText; }, 2000);"}
      "COPY"]]

    ;; All icons with equal spacing - Docker and Sandbox first, then languages
    [:div {:class "flex flex-wrap gap-3 sm:flex-nowrap"}
     ;; Docker (Run locally)
     [:a {:class "p-3 rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-colors duration-300 relative group"
          :href (http/get-product-prefixed-url context "/getting-started/run-aidbox-locally")}
      [:img {:src "/docs/.gitbook/assets/docker-mark-blue.svg" :alt "Run locally" :class "w-8 h-8"}]
      [:span {:class "absolute -top-10 left-1/2 -translate-x-1/2 px-2 py-1 bg-gray-900 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none z-50"}
       "Run Aidbox locally with Docker"]]

     ;; Sandbox - with cloud icon
     [:a {:class "p-3 rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-colors duration-300 relative group"
          :href (http/get-product-prefixed-url context "/getting-started/run-aidbox-in-sandbox")}
      [:img {:src "/docs/.gitbook/assets/cloud.svg" :alt "Run in Sandbox" :class "w-8 h-8"}]
      [:span {:class "absolute -top-10 left-1/2 -translate-x-1/2 px-2 py-1 bg-gray-900 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none z-50"}
       "Try Aidbox in Sandbox"]]

     ;; TypeScript
     [:a {:class "p-3 rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-colors duration-300 relative group"
          :href (http/get-product-prefixed-url context "/getting-started/typescript")}
      [:img {:src "/docs/.gitbook/assets/typescript.svg" :alt "TypeScript" :class "w-8 h-8"}]
      [:span {:class "absolute -top-10 left-1/2 -translate-x-1/2 px-2 py-1 bg-gray-900 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none z-50"}
       "TypeScript SDK Tutorial"]]

     ;; Java
     [:a {:class "p-3 rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-colors duration-300 relative group"
          :href (http/get-product-prefixed-url context "/getting-started/java")}
      [:img {:src "/docs/.gitbook/assets/java.svg" :alt "Java" :class "w-8 h-8"}]
      [:span {:class "absolute -top-10 left-1/2 -translate-x-1/2 px-2 py-1 bg-gray-900 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none z-50"}
       "Java SDK Tutorial"]]

     ;; Python
     [:a {:class "p-3 rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-colors duration-300 relative group"
          :href (http/get-product-prefixed-url context "/getting-started/python")}
      [:img {:src "/docs/.gitbook/assets/python.svg" :alt "Python" :class "w-8 h-8"}]
      [:span {:class "absolute -top-10 left-1/2 -translate-x-1/2 px-2 py-1 bg-gray-900 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none z-50"}
       "Python SDK Tutorial"]]

     ;; C#
          ;; C#
     [:a {:class "p-3 rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-colors duration-300 relative group"
          :href (http/get-product-prefixed-url context "/getting-started/csharp")}
      [:img {:src "/docs/.gitbook/assets/csharp.svg" :alt "C#" :class "w-8 h-8"}]
      [:span {:class "absolute -top-10 left-1/2 -translate-x-1/2 px-2 py-1 bg-gray-900 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none z-50"}
       "C# SDK Tutorial"]]

     ;; MCP
     [:a {:class "p-3 rounded-lg bg-gradient-to-b from-white to-surface-alt border border-outline transition-colors duration-300 relative group"
          :href (http/get-product-prefixed-url context "/modules/other-modules/mcp")}
      [:img {:src "/docs/.gitbook/assets/mcp.png" :alt "MCP" :class "w-8 h-8"}]
      [:span {:class "absolute -top-10 left-1/2 -translate-x-1/2 px-2 py-1 bg-gray-900 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none z-50"}
       "Model Context Protocol (MCP)"]]]

    ;; AI MCP Server button
    [:a {:class "inline-flex self-start h-[47px] items-center rounded-[26px] border border-outline bg-white py-2 pr-3 pl-2 gap-2 hover:border-brand transition-colors"
         :href (http/get-product-prefixed-url context "/developer-experience/ai")}
     [:div {:class "w-7 h-[31px] rounded-[25px] bg-surface-nav-hover flex items-center justify-center text-xs font-medium text-on-surface-secondary"}
      "AI"]
     [:span {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary"}
      "Learn about Aidbox MCP Server and AI prompts"]
     (ico/chevron-right "size-3 text-on-surface-secondary" :outline)]]])

(defn additional-links
  "Additional important documentation links"
  [context]
  [:div {:class "mt-16 mb-12"}
   ;; See also header - left-aligned with Main concepts styling
   [:div {:class "mb-12"}
    [:h2 {:class "text-[28px] font-semibold leading-9 tracking-[-0.03em] text-on-surface-strong font-sans"} "See also"]]

   [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4"}
    ;; Features
    [:a {:class (str card-base-styles " " card-hover-styles " p-6 h-[176px] no-underline")
         :href (http/get-product-prefixed-url context "/readme/features")}
     [:img {:src "/docs/.gitbook/assets/star_icon.svg"
            :alt "Features"
            :class "w-6 h-6 mb-4"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] text-on-surface-strong m-0"} "Features"]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary m-0 mt-2"} "Explore all the features and capabilities that Aidbox offers"]]

    ;; Architecture
    [:a {:class (str card-base-styles " " card-hover-styles " p-6 h-[176px] no-underline")
         :href (http/get-product-prefixed-url context "/readme/architecture")}
     [:img {:src "/docs/.gitbook/assets/architecture_icon.svg"
            :alt "Architecture"
            :class "w-6 h-6 mb-4"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] text-on-surface-strong m-0"} "Architecture"]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary m-0 mt-2"} "Learn about Aidbox's technical architecture and design principles"]]

    ;; Licensing and Support
    [:a {:class (str card-base-styles " " card-hover-styles " p-6 h-[176px] no-underline")
         :href (http/get-product-prefixed-url context "/readme/licensing")}
     [:img {:src "/docs/.gitbook/assets/licensing_and_support_logo.svg"
            :alt "Licensing & Support"
            :class "w-6 h-6 mb-4"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] text-on-surface-strong m-0"} "Licensing & Support"]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary m-0 mt-2"} "Information about licensing options and support services"]]

    ;; Release Notes
    [:a {:class (str card-base-styles " " card-hover-styles " p-6 h-[176px] no-underline")
         :href (http/get-product-prefixed-url context "/overview/release-notes")}
     [:img {:src "/docs/.gitbook/assets/release_notes.svg"
            :alt "Release Notes"
            :class "w-6 h-6 mb-4"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] text-on-surface-strong m-0"} "Release Notes"]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary m-0 mt-2"} "Stay up to date with the latest updates and improvements"]]]])

(defn zulip-community
  "Zulip community section with proper icon"
  []
  [:div {:class "mt-16 mb-12"}
   [:div {:class "bg-surface-alt rounded-lg p-[33px] border border-outline"}
    [:div {:class "flex flex-col md:flex-row items-center justify-between gap-6"}
     [:div {:class "flex items-center gap-4"}
      ;; Zulip icon from external SVG file
      [:div {:class "w-10 h-10 flex items-center justify-center"}
       [:img {:src "/docs/.gitbook/assets/zulip_logo_svg.svg" :alt "Zulip" :class "w-8 h-8"}]]
      [:div
       [:h3 {:class "text-xl leading-8 font-medium text-on-surface-strong m-0 mb-1"} "Join our Zulip Community"]
       [:p {:class "text-sm leading-5 font-normal text-on-surface-secondary m-0"} "Connect with other developers, ask questions, and share your experiences with Aidbox"]]]
     [:a {:class "inline-flex items-center px-5 py-2.5 bg-brand text-white font-medium font-sans rounded-lg hover:bg-brand-hover transition-colors no-underline"
          :href "https://connect.health-samurai.io/"
          :target "_blank"
          :rel "noopener noreferrer"}
      "Join Zulip Workspace"
      (ico/arrow-top-right-on-square "ml-2 w-4 h-4")]]]])

(defn bento-grid
  "Bento grid with feature cards"
  [context]
  [:div
   [:div {:class "mb-12"}
    [:h2 {:class "text-[28px] font-semibold leading-9 tracking-[-0.03em] mb-4 text-on-surface-strong font-sans"} "Main concepts"]]

   ;; Bento grid layout - 4 columns
   [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"}

    ;; FHIR Database Card - spans 2 columns
    [:div {:class (str card-clickable-styles " p-4 lg:col-span-2 group")
           :data-href (http/get-product-prefixed-url context "/database/overview")
           :onclick "if(!event.target.closest('a')) { if(event.ctrlKey || event.metaKey) { window.open(this.dataset.href, '_blank'); } else { window.location.href = this.dataset.href; } }"
           :onmouseenter "window.status = this.dataset.href; return true;"
           :onmouseleave "window.status = ''; return true;"}
     [:img {:class "w-20 h-20 mb-4"
            :src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a442bb239cfcd007e5c_Database%20%2B%20FHIR.svg"
            :alt "FHIR Database"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] mb-3 text-on-surface-strong font-sans"} "FHIR Database"]
     [:div {:class "mb-3"}
      (tags/render-tags
       [{:text "PostgreSQL" :href (http/get-product-prefixed-url context "/database/overview#how-does-aidbox-store-data")}
        {:text "JSONB" :href (http/get-product-prefixed-url context "/database/overview#the-postgresql-jsonb-approach")}
        {:text "Indexes" :href (http/get-product-prefixed-url context "/deployment-and-maintenance/indexes")}
        {:text "Custom resources" :href (http/get-product-prefixed-url context "/tutorials/artifact-registry-tutorials/custom-resources/custom-resources-using-structuredefinition")}
        {:text "SQL on FHIR" :href (http/get-product-prefixed-url context "/modules/sql-on-fhir")}]
       :default)]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary font-content"}
      "Manage FHIR data with the power of PostgreSQL — fully under your control. Aidbox stores resources transparently as JSONB, enabling you to query, join, and aggregate by any element, with full support for transactional operations, reporting, and seamless migrations."]]

    ;; API Card
    [:div {:class (str card-clickable-styles " p-4 group")
           :data-href (http/get-product-prefixed-url context "/api/api-overview")
           :onclick "if(!event.target.closest('a')) { if(event.ctrlKey || event.metaKey) { window.open(this.dataset.href, '_blank'); } else { window.location.href = this.dataset.href; } }"
           :onmouseenter "window.status = this.dataset.href; return true;"
           :onmouseleave "window.status = ''; return true;"}
     [:img {:class "w-20 h-20 mb-4"
            :src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a444fc720f2ad877e7d_API.svg"
            :alt "API"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] mb-3 text-on-surface-strong font-sans"} "API"]
     [:div {:class "mb-3"}
      (tags/render-tags
       [{:text "FHIR" :href (http/get-product-prefixed-url context "/api/api-overview")}
        {:text "SQL" :href (http/get-product-prefixed-url context "/api/rest-api/other/sql-endpoints")}
        {:text "GraphQL" :href (http/get-product-prefixed-url context "/api/graphql-api")}]
       :default)]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary font-content"}
      "Multiple APIs — FHIR, SQL, GraphQL, Bulk, and Subscription — to work efficiently with FHIR data for maximum flexibility and performance."]]

    ;; Artifact Registry Card
    [:div {:class (str card-clickable-styles " p-4 group")
           :data-href (http/get-product-prefixed-url context "/artifact-registry/artifact-registry-overview")
           :onclick "if(!event.target.closest('a')) { if(event.ctrlKey || event.metaKey) { window.open(this.dataset.href, '_blank'); } else { window.location.href = this.dataset.href; } }"
           :onmouseenter "window.status = this.dataset.href; return true;"
           :onmouseleave "window.status = ''; return true;"}
     [:img {:class "w-20 h-20 mb-4"
            :src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a44bf8f6440a9a0bcc2_FHIR%20Artefact%20Registry.svg"
            :alt "Artifact Registry"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] mb-3 text-on-surface-strong font-sans"} "Artifact Registry"]
     [:div {:class "mb-3"}
      (tags/render-tags
       [{:text "IGs" :href (http/get-product-prefixed-url context "/artifact-registry/artifact-registry-overview")}
        {:text "Profiles" :href (http/get-product-prefixed-url context "/artifact-registry/structuredefinition")}
        {:text "Search params" :href (http/get-product-prefixed-url context "/api/rest-api/fhir-search/searchparameter")}]
       :default)]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary font-content"}
      "Multiple FHIR versions: STU3, R4, R5, and R6. 500+ ready-to-use IGs: IPS, national (US, DE, CA, etc.), domain (mCode, Da Vinci, etc.), custom IGs."]]

    ;; Access Control Card
    [:div {:class (str card-clickable-styles " p-4 group")
           :data-href (http/get-product-prefixed-url context "/access-control/access-control")
           :onclick "if(!event.target.closest('a')) { if(event.ctrlKey || event.metaKey) { window.open(this.dataset.href, '_blank'); } else { window.location.href = this.dataset.href; } }"
           :onmouseenter "window.status = this.dataset.href; return true;"
           :onmouseleave "window.status = ''; return true;"}
     [:img {:class "w-20 h-20 mb-4"
            :src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a441cfd9ebadf77b357_AUTH.svg"
            :alt "Access Control"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] mb-3 text-on-surface-strong font-sans"} "Access Control"]
     [:div {:class "mb-3"}
      (tags/render-tags
       [{:text "OAuth 2.0" :href (http/get-product-prefixed-url context "/access-control/authentication/oauth-2-0")}
        {:text "SMART" :href "https://www.health-samurai.io/docs/aidbox/access-control/authorization/smart-on-fhir"}
        {:text "RBAC/ABAC" :href (http/get-product-prefixed-url context "/access-control/authorization#role-based-access-control")}]
       :default)]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary font-content"}
      "Enterprise-grade security with OAuth 2.0, multitenancy, flexible user management, granular access policies, and complete audit trails."]]

    ;; Terminology Card
    [:div {:class (str card-clickable-styles " p-4 group")
           :data-href (http/get-product-prefixed-url context "/terminology-module/overview")
           :onclick "if(!event.target.closest('a')) { if(event.ctrlKey || event.metaKey) { window.open(this.dataset.href, '_blank'); } else { window.location.href = this.dataset.href; } }"
           :onmouseenter "window.status = this.dataset.href; return true;"
           :onmouseleave "window.status = ''; return true;"}
     [:img {:class "w-20 h-20 mb-4"
            :src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a4419fe3f4c5c0e24b5_Translation%20Book.svg"
            :alt "Terminology"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] mb-3 text-on-surface-strong font-sans"} "Terminology"]
     [:div {:class "mb-3"}
      (tags/render-tags
       [{:text "CodeSystems" :href (http/get-product-prefixed-url context "/terminology-module/fhir-terminology/codesystem")}
        {:text "ValueSets" :href (http/get-product-prefixed-url context "/terminology-module/fhir-terminology/valueset")}]
       :default)]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary font-content"}
      "Validate codes and perform fast lookups in ICD-10, SNOMED, LOINC. Use custom code systems and value sets."]]

    ;; Developer Experience Card
    [:div {:class (str card-clickable-styles " p-4 group")
           :data-href (http/get-product-prefixed-url context "/developer-experience/developer-experience-overview")
           :onclick "if(!event.target.closest('a')) { if(event.ctrlKey || event.metaKey) { window.open(this.dataset.href, '_blank'); } else { window.location.href = this.dataset.href; } }"
           :onmouseenter "window.status = this.dataset.href; return true;"
           :onmouseleave "window.status = ''; return true;"}
     [:img {:class "w-20 h-20 mb-4"
            :src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a4478a178659dd16f36_SDK.svg"
            :alt "Developer Experience"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] mb-3 text-on-surface-strong font-sans"} "Developer Experience"]
     [:div {:class "mb-3"}
      (tags/render-tags
       [{:text "Python" :href (http/get-product-prefixed-url context "/getting-started/python")}
        {:text "C#" :href (http/get-product-prefixed-url context "/getting-started/csharp")}
        {:text "TS" :href (http/get-product-prefixed-url context "/getting-started/typescript")}
        {:text "Codegen" :href (http/get-product-prefixed-url context "/developer-experience/developer-experience-overview#use-aidbox-sdks-for-customized-experience")}]
       :default)]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary font-content"}
      "TypeScript, C#, and Python SDKs for easy Aidbox integration and rapid app development. SDK generator for custom toolkits tailored to your stack."]]

    ;; UI Card
    [:div {:class (str card-clickable-styles " p-4 group")
           :data-href (http/get-product-prefixed-url context "/overview/aidbox-ui")
           :onclick "if(!event.target.closest('a')) { if(event.ctrlKey || event.metaKey) { window.open(this.dataset.href, '_blank'); } else { window.location.href = this.dataset.href; } }"
           :onmouseenter "window.status = this.dataset.href; return true;"
           :onmouseleave "window.status = ''; return true;"}
     [:img {:class "w-20 h-20 mb-4"
            :src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a44f6b12fad351dc0d6_UI.svg"
            :alt "UI"}]
     [:h3 {:class "text-lg font-medium leading-8 tracking-[-0.03em] mb-3 text-on-surface-strong font-sans"} "UI"]
     [:div {:class "mb-3"}
      (tags/render-tags
       ["FHIR Viewer"
        {:text "Search params" :href (http/get-product-prefixed-url context "/api/rest-api/fhir-search/searchparameter")}]
       :default)]
     [:p {:class "text-sm leading-[22.75px] font-normal text-on-surface-secondary font-content"}
      "Intuitive UI to work with FHIR data, manage users, clients, access policies, and configure system settings."]]]])

(defn render-landing
  "Render the landing page for a product"
  [ctx]
  (let [request (:request ctx)
        ;; Custom page structure with gray background
        full-page
        [:div.min-h-screen.flex.flex-col
         (main-navigation/nav ctx)
         [:div.mobile-menu-overlay]

         ;; Container for gray background and card that extends beyond it
         [:div {:class "w-full relative"}
          ;; Gray background - ends 50px before card bottom
          [:div {:class "letter-glitch-container"}]

          ;; Ellipse overlay for lighter background effect
          [:div {:class "hero-ellipse-overlay"}]

          ;; Content
          [:div {:class "max-w-screen-2xl mx-auto px-4 md:px-8 pt-8 sm:pt-12 xl:pt-16 pb-8 relative z-10"}
           [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-8 items-start px-6"}
            ;; Left side - Documentation header
            [:div {:class "flex items-center gap-4"}
             [:div {:class "flex flex-col"}
              ;; Terminal prompt above title
              [:div {:class "font-mono font-medium text-base leading-[22.75px] text-brand mb-3"}
               ">_"]
              [:h1 {:class "m-0 mb-2 text-[36px] leading-[36px] tracking-[-0.03em] text-on-surface-strong font-sans font-semibold"}
               "Aidbox Documentation"]
              [:p {:class "m-0 text-on-surface-secondary font-content text-base leading-6 font-normal max-w-[370px]"}
               "Learn how to get up and running with Aidbox through tutorials, APIs and platform resources."]]]

            ;; Right side - Getting Started card
            (getting-started-card ctx)]]]

         ;; Main content area - outside gray background
         [:div.flex-1
          [:div {:class "max-w-screen-2xl mx-auto px-4 md:px-8 py-8"}
           ;; Bento grid with Main Concepts header
           (bento-grid ctx)

           ;; Additional links section with See also header
           (additional-links ctx)

           ;; Zulip community section
           (zulip-community)]]

         ;; Footer
         (layout/site-footer ctx)

         ;; Glitch background scripts and styles
         [:link {:rel "stylesheet" :href (http/get-prefixed-url ctx "/static/glitch-bg.css")}]

         [:script {:src (http/get-prefixed-url ctx "/static/glitch-bg-optimized.js") :defer true}]]

        title "Aidbox Documentation"
        description "Aidbox - FHIR-first healthcare application platform"]

    ;; Return custom response bypassing standard layout
    (http/response1
     (layout/document
      ctx
      full-page
      {:title title
       :description description
       :canonical-url (http/get-absolute-url ctx (:uri request))
       :og-preview nil
       :lastmod nil
       :favicon-url (http/get-product-prefixed-url
                     ctx
                     "/favicon.ico")}))))
