package com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model


enum class ActionIdentifier(val id: Int) {
    IS_DELETE(-2), IS_SAVE(-1), POSITION(0), LOCATION_ACTIVITY(1), MOVEMENT_ACTIVITY(2), BALLOON_ACTIVITY(
        3
    ),
    SHAPES_ACTIVITY(4), ACTION_SIZE(5), LAST_POSITION(6);

}