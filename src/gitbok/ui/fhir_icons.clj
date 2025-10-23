(ns gitbok.ui.fhir-icons
  "SVG icons for FHIR data types")

(defn datatype-icon
  "Icon for complex data types (cube)"
  []
  [:svg
   {:width "16"
    :height "17"
    :viewBox "0 0 16 17"
    :fill "none"
    :xmlns "http://www.w3.org/2000/svg"
    :class "inline-block"}
   [:g
    [:path
     {:d "M3.91406 5.77679L8.00281 8.08304L12.0793 5.77691"
      :stroke "currentColor"
      :stroke-width "1.25"
      :stroke-linecap "round"
      :stroke-linejoin "round"}]
    [:path
     {:d "M8 12.6996V8.08301"
      :stroke "currentColor"
      :stroke-width "1.25"
      :stroke-linecap "round"
      :stroke-linejoin "round"}]
    [:path
     {:d "M14 5.41636C13.9998 5.18254 13.938 4.9529 13.821 4.75047C13.704 4.54803 13.5358 4.37993 13.3333 4.26302L8.66667 1.59636C8.46397 1.47933 8.23405 1.41772 8 1.41772C7.76595 1.41772 7.53603 1.47933 7.33333 1.59636L2.66667 4.26302C2.46418 4.37993 2.29599 4.54803 2.17897 4.75047C2.06196 4.9529 2.00024 5.18254 2 5.41636V10.7497C2.00024 10.9835 2.06196 11.2132 2.17897 11.4156C2.29599 11.618 2.46418 11.7861 2.66667 11.903L7.33333 14.5697C7.53603 14.6867 7.76595 14.7483 8 14.7483C8.23405 14.7483 8.46397 14.6867 8.66667 14.5697L13.3333 11.903C13.5358 11.7861 13.704 11.618 13.821 11.4156C13.938 11.2132 13.9998 10.9835 14 10.7497V5.41636Z"
      :stroke "currentColor"
      :stroke-width "1.25"
      :stroke-linecap "round"
      :stroke-linejoin "round"}]]])

(defn primitive-icon
  "Icon for primitive types (curly braces)"
  []
  [:svg
   {:width "16"
    :height "17"
    :viewBox "0 0 16 17"
    :fill "none"
    :xmlns "http://www.w3.org/2000/svg"
    :class "inline-block"}
   [:path
    {:d "M3.33594 2.74969H4.0026C4.53304 2.74969 5.04175 2.96041 5.41682 3.33548C5.79189 3.71055 6.0026 4.21926 6.0026 4.74969C6.0026 4.21926 6.21332 3.71055 6.58839 3.33548C6.96346 2.96041 7.47217 2.74969 8.0026 2.74969H8.66927"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M8.66927 13.4163H8.0026C7.47217 13.4163 6.96346 13.2056 6.58839 12.8305C6.21332 12.4555 6.0026 11.9468 6.0026 11.4163C6.0026 11.9468 5.79189 12.4555 5.41682 12.8305C5.04175 13.2056 4.53304 13.4163 4.0026 13.4163H3.33594"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M3.33594 10.7497H2.66927C2.31565 10.7497 1.97651 10.6092 1.72646 10.3591C1.47641 10.1091 1.33594 9.76994 1.33594 9.41632V6.74965C1.33594 6.39603 1.47641 6.05689 1.72646 5.80685C1.97651 5.5568 2.31565 5.41632 2.66927 5.41632H3.33594"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M8.66406 5.41632H13.3307C13.6844 5.41632 14.0235 5.5568 14.2735 5.80685C14.5236 6.05689 14.6641 6.39603 14.6641 6.74965V9.41632C14.6641 9.76994 14.5236 10.1091 14.2735 10.3591C14.0235 10.6092 13.6844 10.7497 13.3307 10.7497H8.66406"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M6 4.74969V11.4164"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]])

(defn choice-icon
  "Icon for union/choice types (diamond with question mark)"
  []
  [:svg
   {:width "16"
    :height "17"
    :viewBox "0 0 16 17"
    :fill "none"
    :xmlns "http://www.w3.org/2000/svg"
    :class "inline-block text-[#405CBF]"}
   [:g
    [:path
     {:d "M1.21109 6.84206C1.04758 7.0054 0.917866 7.19937 0.829365 7.41287C0.740865 7.62638 0.695313 7.85523 0.695312 8.08635C0.695313 8.31747 0.740865 8.54632 0.829365 8.75983C0.917866 8.97333 1.04758 9.1673 1.21109 9.33064L6.7502 14.8698C6.91354 15.0333 7.10751 15.163 7.32101 15.2515C7.53452 15.34 7.76337 15.3855 7.99449 15.3855C8.22561 15.3855 8.45446 15.34 8.66797 15.2515C8.88147 15.163 9.07544 15.0333 9.23878 14.8698L14.7779 9.33064C14.9414 9.1673 15.0711 8.97333 15.1596 8.75983C15.2481 8.54632 15.2937 8.31747 15.2937 8.08635C15.2937 7.85523 15.2481 7.62638 15.1596 7.41287C15.0711 7.19937 14.9414 7.0054 14.7779 6.84206L9.23878 1.30295C9.07544 1.13944 8.88147 1.00972 8.66797 0.921223C8.45446 0.832723 8.22561 0.78717 7.99449 0.78717C7.76337 0.78717 7.53452 0.832723 7.32101 0.921223C7.10751 1.00972 6.91354 1.13944 6.7502 1.30295L1.21109 6.84206Z"
      :stroke "currentColor"
      :stroke-width "1.25"
      :stroke-linecap "round"
      :stroke-linejoin "round"}]
    [:path
     {:fill-rule "evenodd"
      :clip-rule "evenodd"
      :d "M8.18179 5.7278C7.86175 5.67291 7.5326 5.73305 7.25265 5.89758C6.9727 6.06211 6.76001 6.32041 6.65225 6.62673C6.53771 6.95235 6.18089 7.12346 5.85527 7.00891C5.52965 6.89437 5.35854 6.53754 5.47309 6.21192C5.6788 5.62713 6.08484 5.13402 6.6193 4.81992C7.15375 4.50581 7.78212 4.391 8.39312 4.4958C9.00411 4.6006 9.5583 4.91826 9.95753 5.39251C10.3567 5.8667 10.5752 6.46684 10.5743 7.08667C10.574 8.08475 9.83365 8.74761 9.29602 9.10602C9.00813 9.29795 8.72468 9.43923 8.51567 9.53213C8.41028 9.57897 8.32158 9.61449 8.25777 9.6388C8.08575 9.70433 8.22306 9.66463 8.14777 9.67866C8.20338 9.66012 8.22643 9.6509 8.14777 9.67866C7.8203 9.78781 7.46556 9.6111 7.35641 9.28364C7.2474 8.9566 7.42377 8.60314 7.75042 8.49349L7.75172 8.49305L7.76242 8.4893C7.77295 8.48555 7.79005 8.47935 7.81278 8.47069C7.85834 8.45333 7.92589 8.42635 8.008 8.38986C8.17399 8.31609 8.39054 8.20737 8.60265 8.06596C9.0649 7.7578 9.32434 7.42087 9.32434 7.08599C9.32482 6.76127 9.21037 6.44593 9.00125 6.19751C8.79213 5.94909 8.50184 5.7827 8.18179 5.7278Z"
      :fill "currentColor"}]
    [:path
     {:fill-rule "evenodd"
      :clip-rule "evenodd"
      :d "M7.375 11.7526C7.375 11.4074 7.65482 11.1276 8 11.1276H8.00667C8.35184 11.1276 8.63167 11.4074 8.63167 11.7526C8.63167 12.0978 8.35184 12.3776 8.00667 12.3776H8C7.65482 12.3776 7.375 12.0978 7.375 11.7526Z"
      :fill "currentColor"}]]])

(defn resource-icon
  "Icon for resource types (three boxes)"
  []
  [:svg
   {:width "16"
    :height "17"
    :viewBox "0 0 16 17"
    :fill "none"
    :xmlns "http://www.w3.org/2000/svg"
    :class "inline-block"}
   [:path
    {:d "M13.9974 11.167H11.3307C10.9625 11.167 10.6641 11.4655 10.6641 11.8337V14.5003C10.6641 14.8685 10.9625 15.167 11.3307 15.167H13.9974C14.3656 15.167 14.6641 14.8685 14.6641 14.5003V11.8337C14.6641 11.4655 14.3656 11.167 13.9974 11.167Z"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M4.66927 11.167H2.0026C1.63441 11.167 1.33594 11.4655 1.33594 11.8337V14.5003C1.33594 14.8685 1.63441 15.167 2.0026 15.167H4.66927C5.03746 15.167 5.33594 14.8685 5.33594 14.5003V11.8337C5.33594 11.4655 5.03746 11.167 4.66927 11.167Z"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M9.33333 1.83301H6.66667C6.29848 1.83301 6 2.13148 6 2.49967V5.16634C6 5.53453 6.29848 5.83301 6.66667 5.83301H9.33333C9.70152 5.83301 10 5.53453 10 5.16634V2.49967C10 2.13148 9.70152 1.83301 9.33333 1.83301Z"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M3.33594 11.1667V9.16667C3.33594 8.98986 3.40618 8.82029 3.5312 8.69526C3.65622 8.57024 3.82579 8.5 4.0026 8.5H12.0026C12.1794 8.5 12.349 8.57024 12.474 8.69526C12.599 8.82029 12.6693 8.98986 12.6693 9.16667V11.1667"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M8 8.49967V5.83301"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]])

(defn external-link-icon
  "Icon for Reference types (arrow)"
  []
  [:svg
   {:xmlns "http://www.w3.org/2000/svg"
    :width "17"
    :height "17"
    :viewBox "0 0 17 17"
    :fill "none"
    :class "inline-block text-[#405CBF]"}
   [:path {:d "M10.4004 2.5H14.4004V6.5" :fill "white"}]
   [:path
    {:d "M10.4004 2.5H14.4004V6.5"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M7.06641 9.83333L14.3997 2.5L7.06641 9.83333Z" :fill "white"}]
   [:path
    {:d "M7.06641 9.83333L14.3997 2.5"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M12.4004 9.16667V13.1667C12.4004 13.5203 12.2599 13.8594 12.0099 14.1095C11.7598 14.3595 11.4207 14.5 11.0671 14.5H3.73372C3.3801 14.5 3.04096 14.3595 2.79091 14.1095C2.54087 13.8594 2.40039 13.5203 2.40039 13.1667V5.83333C2.40039 5.47971 2.54087 5.14057 2.79091 4.89052C3.04096 4.64048 3.3801 4.5 3.73372 4.5H7.73372"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]])

(defn slice-item-icon
  "Icon for extensions/slices (cylinders)"
  []
  [:svg
   {:width "16"
    :height "17"
    :viewBox "0 0 16 17"
    :fill "none"
    :xmlns "http://www.w3.org/2000/svg"
    :class "inline-block text-[#00A984]"}
   [:path
    {:d "M8 8.41632C11.3137 8.41632 14 7.52089 14 6.41632C14 5.31175 11.3137 4.41632 8 4.41632C4.68629 4.41632 2 5.31175 2 6.41632C2 7.52089 4.68629 8.41632 8 8.41632Z"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]
   [:path
    {:d "M2 6.41632V9.74965C2 10.2801 2.63214 10.7888 3.75736 11.1639C4.88258 11.5389 6.4087 11.7497 8 11.7497C9.5913 11.7497 11.1174 11.5389 12.2426 11.1639C13.3679 10.7888 14 10.2801 14 9.74965V6.41632"
     :stroke "currentColor"
     :stroke-width "1.25"
     :stroke-linecap "round"
     :stroke-linejoin "round"}]])

(def primitive-types
  #{"boolean" "integer" "string" "decimal" "uri" "url" "canonical"
    "base64Binary" "instant" "date" "dateTime" "time" "code" "oid"
    "id" "markdown" "unsignedInt" "positiveInt" "uuid" "xhtml"})

(defn type-icon
  "Returns the appropriate icon for a given data type"
  [element]
  (cond
    ;; Root/resource type
    (= "root" (:type element))
    (resource-icon)

    ;; Extensions or slices
    (or (:extension-url element)
        (:slice-type element))
    (slice-item-icon)

    ;; Primitive types
    (contains? primitive-types (:type element))
    (primitive-icon)

    ;; Union/choice types (fields with [x])
    (:union? element)
    (choice-icon)

    ;; Reference types
    (= "Reference" (:type element))
    (external-link-icon)

    ;; Default: complex datatype
    :else
    (datatype-icon)))
