# How to customize form colors

Aidbox Forms gives you ability to customize form looking. To do this you can create a new theme, and specify it in sdc-service.

The simplest variant of forms theme looks like this:&#x20;

```clojure
simplest-sdc-theme
{:zen/tags #{aidbox.sdc/theme}
 :zen/desc "Default SDC theme"
 :main-color [120 38 245]}
```

> Each color in theme represented as vector with three RGB channels: \[<mark style="color:red;">RED</mark> <mark style="color:green;">GREEN</mark> <mark style="color:blue;">BLUE</mark>]

To use this theme specify `simplest-sdc-theme` under the `:theme` key in SDC service.

In this theme we change only main-color, but this color changes a lot in Aidbox Forms looking: inputs border color, buttons background color, etc. So you can use just one key to style forms with your brand color. &#x20;

If you need more detailed styling, you can use extended theme definition. With this you can change separately buttons main color, text color, input border, background, text colors and background colors.&#x20;

To change buttons appearance add to theme scheme a new map under `:button` key:

```clojure
buttons-sdc-theme
{:zen/tags #{aidbox.sdc/theme}
 :zen/desc "Default SDC theme"
 :main-color [120 38 245]
 :button {:text-color [0 94 184]
          :accent-color [255 255 255]}}
```

For filled buttons accent color will be used as background color, for outlined buttons as border color and text color.&#x20;

To change inputs appearance add to theme scheme a new map under `:input` key:

```
inputs-sdc-theme
{:zen/tags #{aidbox.sdc/theme}
 :zen/desc "Theme styled with NHS colors"
 :main-color [0 94 184]
 :input {:accent-color [0 94 184]
         :text-color [35 31 32]
         :background-color [232 237 238]}}
```

Here accent color will be used as border color on hover, focused state.&#x20;

Also you can change background colors:

```clojure
background-sdc-theme
{:zen/tags #{aidbox.sdc/theme}
 :zen/desc "Theme styled with NHS colors"
 :main-color [0 94 184]
 :background {:main-color [232 237 238]
              :form-color [255 255 255]
              :toolbar-color [118 134 146]}}
```

To understand what each color is look at the image below:

<figure><img src="../../.gitbook/assets/CleanShot 2023-02-16 at 11.34.05.png" alt=""><figcaption></figcaption></figure>

So full theme look like this:&#x20;

```clojure
nhs-sdc-theme
{:zen/tags #{aidbox.sdc/theme}
 :zen/desc "Theme styled with NHS colors"
 :main-color [0 94 184]
 :background {:main-color [232 237 238]
              :form-color [255 255 255]
              :toolbar-color [118 134 146]}
 :input {:accent-color [0 94 184]
         :text-color [35 31 32]
         :background-color [232 237 238]}
 :button {:accent-color [0 94 184]
          :text-color [255 255 255]}}
```
