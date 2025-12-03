!function(t,e){var o,n,p,r;e.__SV||(window.posthog=e,e._i=[],e.init=function(i,s,a){function g(t,e){var o=e.split(".");2==o.length&&(t=t[o[0]],e=o[1]),t[e]=function(){t.push([e].concat(Array.prototype.slice.call(arguments,0)))}}(p=t.createElement("script")).type="text/javascript",p.crossOrigin="anonymous",p.async=!0,p.src=s.api_host.replace(".i.posthog.com","-assets.i.posthog.com")+"/static/array.js",(r=t.getElementsByTagName("script")[0]).parentNode.insertBefore(p,r);var u=e;for(void 0!==a?u=e[a]=[]:a="posthog",u.people=u.people||[],u.toString=function(t){var e="posthog";return"posthog"!==a&&(e+="."+a),t||(e+=" (stub)"),e},u.people.toString=function(){return u.toString(1)+".people (stub)"},o="init capture register register_once register_for_session unregister unregister_for_session getFeatureFlag getFeatureFlagPayload isFeatureEnabled reloadFeatureFlags updateEarlyAccessFeatureEnrollment getEarlyAccessFeatures on onFeatureFlags onSessionId getSurveys getActiveMatchingSurveys renderSurvey canRenderSurvey getNextSurveyStep identify setPersonProperties group resetGroups setPersonPropertiesForFlags resetPersonPropertiesForFlags setGroupPropertiesForFlags resetGroupPropertiesForFlags reset get_distinct_id getGroups get_session_id get_session_replay_url alias set_config startSessionRecording stopSessionRecording sessionRecordingStarted captureException loadToolbar get_property getSessionProperty createPersonProfile opt_in_capturing opt_out_capturing has_opted_in_capturing has_opted_out_capturing clear_opt_in_out_capturing debug".split(" "),n=0;n<o.length;n++)g(u,o[n]);e._i.push([i,s,a])},e.__SV=1)}(document,window.posthog||[]);

// List of domains to track
var TRACKED_DOMAINS = [
    'health-samurai.io',
    'health-samurai.ru',
    'aidbox.app',
    'fhir.ru',
    'fhirstarter.com',
    'docs.aidbox.app',
    'form-builder.aidbox.app',
    'sqlonfhir.aidbox.app'
];

(function(){
    // Check for PostHog tracking parameters in URL
    var initPostHogWithParams = function() {
        try {
            var urlParams = new URLSearchParams(window.location.search);
            var sessionId = urlParams.get('_phsi');
            var distinctId = urlParams.get('_phdi');
            // Check if user has rejected cookies - use memory-only persistence
            var consentStatus = localStorage.getItem('cookie_consent');
            var posthogParams = {
                api_host: 'https://ph.aidbox.app',
                defaults: '2025-05-24',
                person_profiles: 'identified_only',
                persistence: consentStatus === 'accepted' ? 'localStorage+cookie' : 'memory'
            };

            if (sessionId && distinctId && typeof posthog !== 'undefined') {
                posthogParams.bootstrap = {
                    sessionID: sessionId,
                    distinctID: distinctId
                }
            };

            posthog.init("phc_uO4ImMUxOljaWPDRr7lWu9TYpBrpIs4R1RwLu8uLRmx", posthogParams);

        } catch(e) {return;}
    };

    var initTracking = function() {
        if(typeof posthog === 'undefined') return;

        var handleClick = function(e) {
            var target = e.target.closest('a');
            if(!target || !target.href) return;
            try {
                var url = new URL(target.href);
                if(!TRACKED_DOMAINS.includes(url.hostname)) return;
                if(url.hostname === window.location.hostname) return;

                var sessionId = posthog.get_session_id();
                var distinctId = posthog.get_distinct_id();

                if(sessionId && distinctId) {
                    url.searchParams.set('_phsi', sessionId);
                    url.searchParams.set('_phdi', distinctId);
                }
                target.href = url.toString();
            } catch(e) {return;}
        };

        document.addEventListener('click', handleClick);
    };

    // Initialize both tracking features
    if(document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            initPostHogWithParams();
            initTracking();
        });
    } else {
        initPostHogWithParams();
        initTracking();
    }
})();
