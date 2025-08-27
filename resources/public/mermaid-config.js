// Centralized Mermaid configuration
window.MERMAID_CONFIG = {
  startOnLoad: false, // We'll manually trigger rendering
  theme: 'base',
  securityLevel: 'loose',
  themeVariables: {
    fontFamily: 'Inter, verdana',
    lineColor: '#717684',
    secondaryColor: '#EBECEE',
    tertiaryColor: '#fff',
    primaryColor : "#FEF2F2",
    primaryTextColor : "#1D2331",
    primaryBorderColor : "#F58685",
    sequenceNumberColor : "#78B58E",
    activationBkgColor : "#CCCED4",
    activationBorderColor : "#fff",
    loopTextColor : "#F58685",
    labelTextColor : "#CC88FF",
    labelBoxBorderColor : "#99FFCC",
    labelBoxBkgColor : "#78B58E",
    noteBkgColor : "#F5F5F6",
    edgeLabelBackground:'#fff'
  },
  themeCSS: `
    /* ==== RED ==== */
    .node.red1 rect { fill:#fef2f2; stroke:#F58685; stroke-width:1px; }
    .node.red2 rect { fill:#fef2f2; stroke:#F58685; stroke-width:2px; }
    .node.red3 rect { fill:#fef2f2; stroke:#F58685; stroke-width:3px; }

    /* ==== BLUE ==== */
    .node.blue1 rect { fill:#eff6ff; stroke:#7DA1EF; stroke-width:1px; }
    .node.blue2 rect { fill:#eff6ff; stroke:#7DA1EF; stroke-width:2px; }
    .node.blue3 rect { fill:#eff6ff; stroke:#7DA1EF; stroke-width:3px; }

    /* ==== VIOLET ==== */
    .node.violet1 rect { fill:#faf5ff; stroke:#AB8AE3; stroke-width:1px; }
    .node.violet2 rect { fill:#faf5ff; stroke:#AB8AE3; stroke-width:2px; }
    .node.violet3 rect { fill:#faf5ff; stroke:#AB8AE3; stroke-width:3px; }

    /* ==== GREEN ==== */
    .node.green1 rect { fill:#f0fdf4; stroke:#78B58E; stroke-width:1px; }
    .node.green2 rect { fill:#f0fdf4; stroke:#78B58E; stroke-width:2px; }
    .node.green3 rect { fill:#f0fdf4; stroke:#78B58E; stroke-width:3px; }

    /* ==== YELLOW ==== */
    .node.yellow1 rect { fill:#fefce8; stroke:#E4BE6F; stroke-width:1px; }
    .node.yellow2 rect { fill:#fefce8; stroke:#E4BE6F; stroke-width:2px; }
    .node.yellow3 rect { fill:#fefce8; stroke:#E4BE6F; stroke-width:3px; }

    /* ==== NEUTRAL ==== */
    .node.neutral1 rect { fill:#F5F5F6; stroke:#CCCED3; stroke-width:1px; }
    .node.neutral2 rect { fill:#F5F5F6; stroke:#CCCED3; stroke-width:2px; }
    .node.neutral3 rect { fill:#F5F5F6; stroke:#CCCED3; stroke-width:3px; }

    /* ==== LINK LABELS ==== */
    .edgeLabel.linkLabelNeutral { fill:#1D2331; background:#F5F5F6; stroke:#CCCED3; stroke-width:1px; }
    .edgeLabel.linkLabelSuccess { fill:#1D2331; background:#f0fdf4; stroke:#16a34a; stroke-width:1px; }
    .edgeLabel.linkLabelError { fill:#1D2331; background:#fef2f2; stroke:#dc2626; stroke-width:1px; }
  `
};
