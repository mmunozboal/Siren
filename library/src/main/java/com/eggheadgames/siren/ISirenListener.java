package com.eggheadgames.siren;

@SuppressWarnings("WeakerAccess")
public interface ISirenListener {

    void onShowUpdateDialog();                       // User presented with update dialog
    void onLaunchGooglePlay();                       // User did click on button that launched Google Play
    void onSkipVersion();                            // User did click on button that skips version update
    void onCancel();                                 // User did click on button that cancels update dialog
    void onDetectNewVersionWithoutAlert(String message); // Siren performed version check and did not display alert
    void onError(Exception e);
}
