package com.eggheadgames.siren;

/**
 * Determines the type of alert to present after a successful version check has been performed.
 */
public enum SirenAlertType {
    FORCE,                  //Forces user to update your app (1 button alert)
    OPTION,                 //DEFAULT) Presents user with option to update app now or at next launch (2 button alert)
    SKIP,                   //Presents user with option to update the app now, at next launch, or to skip this version all together (3 button alert)
    NONE                    //Doesn't show the alert, but instead returns a localized message for use in a custom UI within the onDetectNewVersionWithoutAlert() callback
}
