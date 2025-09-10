(ns gitbok.ui.landing-hero
  (:require [gitbok.http]
            [gitbok.ui.layout :as layout]
            [uui.heroicons :as ico]))

(defn getting-started-card
  "Getting Started card with tutorial icons"
  [context]
  [:div.border.bg-tint-base.relative.overflow-hidden.rounded-lg.p-6.border-tint-6
   [:div.flex.flex-col.gap-4
    [:div.flex.items-center.gap-3
     [:div.shrink-0.bg-primary-2.border.border-primary-3.w-10.h-10.flex.items-center.justify-center.rounded
      (ico/play "text-primary-9 w-5 h-5")]
     [:div
      [:h2.text-2xl.m-0.text-tint-12.font-semibold "Getting Started"]
      [:p.text-tint-10.text-sm.m-0.mt-1 "Get started in just a few minutes."]]]

    ;; One-liner command
    [:pre {:class "text-base"}
     [:code
      "curl -JO https://aidbox.app/runme && docker compose up"]]

    ;; All icons with equal spacing - Docker and Sandbox first, then languages
    [:div.flex.justify-between
     ;; Docker (Run locally)
     [:a.p-3.rounded-lg.border.border-tint-6.hover:border-primary-9.transition.relative.group
      {:href (gitbok.http/get-product-prefixed-url context "/getting-started/run-aidbox-locally")
       :title "Run Aidbox locally with Docker"}
      [:img {:src "/docs/.gitbook/assets/docker-mark-blue.svg" :alt "Run locally" :class "w-8 h-8"}]]

     ;; Sandbox
     [:a.p-3.rounded-lg.border.border-tint-6.hover:border-primary-9.transition.relative.group
      {:href (gitbok.http/get-product-prefixed-url context "/getting-started/run-aidbox-in-sandbox")
       :title "Try Aidbox in Sandbox"}
      [:img {:src "/docs/.gitbook/assets/sandbox-svgrepo-com.svg" :alt "Run in Sandbox" :class "w-8 h-8"}]]

     ;; TypeScript
     [:a.p-3.rounded-lg.border.border-tint-6.hover:border-primary-9.transition.relative.group
      {:href (gitbok.http/get-product-prefixed-url context "/getting-started/typescript")
       :title "TypeScript SDK Tutorial"}
      [:img {:src "/docs/.gitbook/assets/typescript.svg" :alt "TypeScript" :class "w-8 h-8"}]]

     ;; Java
     [:a.p-3.rounded-lg.border.border-tint-6.hover:border-primary-9.transition.relative.group
      {:href (gitbok.http/get-product-prefixed-url context "/getting-started/java")
       :title "Java SDK Tutorial"}
      [:img {:src "/docs/.gitbook/assets/java.svg" :alt "Java" :class "w-8 h-8"}]]

     ;; Python
     [:a.p-3.rounded-lg.border.border-tint-6.hover:border-primary-9.transition.relative.group
      {:href (gitbok.http/get-product-prefixed-url context "/getting-started/python")
       :title "Python SDK Tutorial"}
      [:img {:src "/docs/.gitbook/assets/python.svg" :alt "Python" :class "w-8 h-8"}]]

     ;; C#
     [:a.p-3.rounded-lg.border.border-tint-6.hover:border-primary-9.transition.relative.group
      {:href (gitbok.http/get-product-prefixed-url context "/getting-started/csharp")
       :title "C# SDK Tutorial"}
      [:img {:src "/docs/.gitbook/assets/csharp.svg" :alt "C#" :class "w-8 h-8"}]]]]])

(defn additional-links
  "Additional important documentation links"
  [context]
  [:div.mt-16.mb-12
   ;; See also header - centered
   [:div.text-center.mb-8
    [:h2.text-2xl.font-bold.text-tint-12.font-sans "See also"]]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-4.gap-4
    ;; Features
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.hover:border-primary-7.transition-all.no-underline
     {:href (gitbok.http/get-product-prefixed-url context "/readme/features")}
     [:h3.text-lg.font-semibold.text-tint-12.m-0 "Features"]
     [:p.text-sm.text-tint-10.m-0.mt-2 "Explore all the features and capabilities that Aidbox offers"]]

    ;; Architecture
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.hover:border-primary-7.transition-all.no-underline
     {:href (gitbok.http/get-product-prefixed-url context "/readme/architecture")}
     [:h3.text-lg.font-semibold.text-tint-12.m-0 "Architecture"]
     [:p.text-sm.text-tint-10.m-0.mt-2 "Learn about Aidbox's technical architecture and design principles"]]

    ;; Licensing and Support
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.hover:border-primary-7.transition-all.no-underline
     {:href (gitbok.http/get-product-prefixed-url context "/readme/licensing")}
     [:h3.text-lg.font-semibold.text-tint-12.m-0 "Licensing & Support"]
     [:p.text-sm.text-tint-10.m-0.mt-2 "Information about licensing options and support services"]]

    ;; Release Notes
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.hover:border-primary-7.transition-all.no-underline
     {:href (gitbok.http/get-product-prefixed-url context "/release-notes")}
     [:h3.text-lg.font-semibold.text-tint-12.m-0 "Release Notes"]
     [:p.text-sm.text-tint-10.m-0.mt-2 "Stay up to date with the latest updates and improvements"]]]])

(defn zulip-community
  "Zulip community section with proper icon"
  []
  [:div.mt-16.mb-12
   [:div.bg-tint-2.rounded-lg.p-8.border.border-tint-6
    [:div.flex.flex-col.md:flex-row.items-center.justify-between.gap-6
     [:div.flex.items-center.gap-4
      ;; Zulip icon from external SVG file
      [:div.w-10.h-10.flex.items-center.justify-center
       [:img {:src "/docs/.gitbook/assets/zulip.svg" :alt "Zulip" :class "w-8 h-8"}]]
      [:div
       [:h3.text-xl.font-semibold.text-tint-12.m-0.mb-1 "Join our Zulip Community"]
       [:p.text-sm.text-tint-10.m-0 "Connect with other developers, ask questions, and share your experiences with Aidbox"]]]
     [:a.inline-flex.items-center.px-5.py-2.5.bg-primary-9.text-white.font-medium.font-sans.rounded-lg.hover:bg-primary-10.transition-colors.no-underline
      {:href "https://connect.health-samurai.io/" :target "_blank" :rel "noopener noreferrer"}
      "Join Zulip Workspace"
      (ico/arrow-top-right-on-square "ml-2 w-4 h-4")]]]])

(defn bento-grid
  "Bento grid with feature cards"
  [context]
  [:div
   [:div.text-center.mb-12
    [:h2.text-3xl.font-bold.mb-4.text-tint-12.font-sans "Main concepts"]]

   ;; Bento grid layout - 4 columns
   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-4.gap-6

    ;; FHIR Database Card - spans 2 columns
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300.lg:col-span-2
     {:href (gitbok.http/get-product-prefixed-url context "/database/overview")}
     [:img.w-20.h-20.mb-4
      {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a442bb239cfcd007e5c_Database%20%2B%20FHIR.svg"
       :alt "FHIR Database"}]
     [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "FHIR Database"]
     [:div.flex.flex-wrap.gap-2.mb-3
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/database/overview#how-does-aidbox-store-data") "'")}
       "PostgreSQL"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/database/overview#the-postgresql-jsonb-approach") "'")}
       "JSONB"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/deployment-and-maintenance/indexes") "'")}
       "Indexes"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/tutorials/artifact-registry-tutorials/custom-resources/custom-resources-using-structuredefinition") "'")}
       "Custom resources"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/modules/sql-on-fhir") "'")}
       "SQL on FHIR"]]
     [:p.text-sm.leading-relaxed.text-tint-10.font-content
      "Manage FHIR data with the power of PostgreSQL — fully under your control. Aidbox stores resources transparently as JSONB, enabling you to query, join, and aggregate by any element, with full support for transactional operations, reporting, and seamless migrations."]]

    ;; API Card
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
     {:href (gitbok.http/get-product-prefixed-url context "/api/api-overview")}
     [:img.w-20.h-20.mb-4
      {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a444fc720f2ad877e7d_API.svg"
       :alt "API"}]
     [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "API"]
     [:div.flex.flex-wrap.gap-2.mb-3
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/api/api-overview") "'")}
       "FHIR"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "SQL"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/api/graphql-api") "'")}
       "GraphQL"]]
     [:p.text-sm.leading-relaxed.text-tint-10.font-content
      "Multiple APIs — FHIR, SQL, GraphQL, Bulk, and Subscription — to work efficiently with FHIR data for maximum flexibility and performance."]]

    ;; Artifact Registry Card
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
     {:href (gitbok.http/get-product-prefixed-url context "/artifact-registry/artifact-registry-overview")}
     [:img.w-20.h-20.mb-4
      {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a44bf8f6440a9a0bcc2_FHIR%20Artefact%20Registry.svg"
       :alt "Artifact Registry"}]
     [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Artifact Registry"]
     [:div.flex.flex-wrap.gap-2.mb-3
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/artifact-registry/artifact-registry-overview") "'")}
       "IGs"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/artifact-registry/structuredefinition") "'")}
       "Profiles"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/api/rest-api/fhir-search/searchparameter") "'")}
       "Search params"]]
     [:p.text-sm.leading-relaxed.text-tint-10.font-content
      "Multiple FHIR versions: STU3, R4, R5, and R6. 500+ ready-to-use IGs: IPS, national (US, DE, CA, etc.), domain (mCode, Da Vinci, etc.), custom IGs."]]

    ;; Access Control Card
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
     {:href (gitbok.http/get-product-prefixed-url context "/access-control/access-control")}
     [:img.w-20.h-20.mb-4
      {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a441cfd9ebadf77b357_AUTH.svg"
       :alt "Access Control"}]
     [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Access Control"]
     [:div.flex.flex-wrap.gap-2.mb-3
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/access-control/authentication/oauth-2-0") "'")}
       "OAuth 2.0"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick "event.stopPropagation(); window.open('https://www.health-samurai.io/docs/aidbox/access-control/authorization/smart-on-fhir', '_blank')"}
       "SMART"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/access-control/authorization#role-based-access-control") "'")}
       "RBAC/ABAC"]]
     [:p.text-sm.leading-relaxed.text-tint-10.font-content
      "Enterprise-grade security with OAuth 2.0, multitenancy, flexible user management, granular access policies, and complete audit trails."]]

    ;; Terminology Card
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
     {:href (gitbok.http/get-product-prefixed-url context "/terminology-module/overview")}
     [:img.w-20.h-20.mb-4
      {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a4419fe3f4c5c0e24b5_Translation%20Book.svg"
       :alt "Terminology"}]
     [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Terminology"]
     [:div.flex.flex-wrap.gap-2.mb-3
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/terminology-module/fhir-terminology/codesystem") "'")}
       "CodeSystems"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/terminology-module/fhir-terminology/valueset") "'")}
       "ValueSets"]]
     [:p.text-sm.leading-relaxed.text-tint-10.font-content
      "Validate codes and perform fast lookups in ICD-10, SNOMED, LOINC. Use custom code systems and value sets."]]

    ;; Developer Experience Card
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
     {:href (gitbok.http/get-product-prefixed-url context "/developer-experience/developer-experience-overview")}
     [:img.w-20.h-20.mb-4
      {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a4478a178659dd16f36_SDK.svg"
       :alt "Developer Experience"}]
     [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Developer Experience"]
     [:div.flex.flex-wrap.gap-2.mb-3
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/getting-started/python") "'")}
       "Python"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/getting-started/csharp") "'")}
       "C#"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/getting-started/typescript") "'")}
       "TS"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded.hover:bg-tint-4.transition-colors.cursor-pointer
       {:onclick (str "event.stopPropagation(); window.location.href='" (gitbok.http/get-product-prefixed-url context "/developer-experience/developer-experience-overview#use-aidbox-sdks-for-customized-experience") "'")}
       "Codegen"]]
     [:p.text-sm.leading-relaxed.text-tint-10.font-content
      "TypeScript, C#, and Python SDKs for easy Aidbox integration and rapid app development. SDK generator for custom toolkits tailored to your stack."]]

    ;; UI Card
    [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
     {:href (gitbok.http/get-product-prefixed-url context "/overview/aidbox-ui")}
     [:img.w-20.h-20.mb-4
      {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a44f6b12fad351dc0d6_UI.svg"
       :alt "UI"}]
     [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "UI"]
     [:div.flex.flex-wrap.gap-2.mb-3
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "FHIR Viewer"]
      [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Search params"]]
     [:p.text-sm.leading-relaxed.text-tint-10.font-content
      "Intuitive UI to work with FHIR data, manage users, clients, access policies, and configure system settings."]]]])

(defn render-landing
  "Render the landing page for a product"
  [context request]
  (let [content
        [:div.px-4.mx-auto
         ;; Documentation header and Getting Started in one section
         [:div.relative.z-10.w-full.bg-tint-base.border-tint-6.mb-8
          [:div.max-w-7xl.px-5.mx-auto.sm:pb-12.sm:pt-12.xl:pt-16
           [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.items-start
            ;; Left side - Documentation header
            [:div.flex.items-center.gap-4
             [:img {:alt "Aidbox Logo"
                    :class "block object-contain size-22"
                    :src (gitbok.http/get-absolute-url context "/.gitbook/assets/aidbox_logo.jpg")}]
             [:div.flex.flex-col
              [:h1.m-0.mb-2.text-3xl.sm:text-4xl.text-tint-12.font-sans "Aidbox Documentation"]
              [:p.m-0.text-tint-10.font-content.text-base
               "Learn how to get up and running with Aidbox through tutorials, APIs and platform resources."]]]

            ;; Right side - Getting Started card
            (getting-started-card context)]]]

         ;; Bento grid with Main Concepts header
         (bento-grid context)

         ;; Additional links section with See also header
         (additional-links context)

         ;; Zulip community section
         (zulip-community)]

        title "Aidbox Documentation"
        description "Aidbox - FHIR-first healthcare application platform"

        ;; Use shared page-wrapper for consistent layout
        full-page (layout/page-wrapper context content)]

    ;; Return custom response bypassing standard layout
    (gitbok.http/response1
     (layout/document
      context
      full-page
      {:title title
       :description description
       :canonical-url (gitbok.http/get-absolute-url context (:uri request))
       :og-preview nil
       :lastmod nil
       :favicon-url (gitbok.http/get-product-prefixed-url
                     context
                     "/favicon.ico")}))))
