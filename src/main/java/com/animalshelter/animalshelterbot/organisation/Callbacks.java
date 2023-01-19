package com.animalshelter.animalshelterbot.organisation;

public enum Callbacks {
    DUMMY("dummy"),

    START_MENU("start_menu"),

    DOG_MENU("dog_menu"),
    CAT_MENU("cat_menu"),

    DOG_INFO_MENU("dog_info_menu"),
    DOG_ADOPTION_INFO_MENU("dog_adoption_info_menu"),
    DOG_REPORT("dog_report"),

    CAT_INFO_MENU("cat_info_menu"),
    CAT_ADOPTION_INFO_MENU("cat_adoption_info_menu"),
    CAT_REPORT("cat_report"),
    CALL_VOLUNTEER("call_volunteer"),

    DOG_SHELTER_INFO("dog_shelter_info"),
    DOG_SCHEDULE_INFO("dog_schedule_info"),
    DOG_CAR_INFO("dog_car_info"),
    DOG_SHELTER_SAFETY_INFO("dog_shelter_safety_info"),
    DOG_CONTACT_INFO("dog_contact_info"),

    CAT_SHELTER_INFO("cat_shelter_info"),
    CAT_SCHEDULE_INFO("cat_schedule_info"),
    CAT_CAR_INFO("cat_car_info"),
    CAT_SHELTER_SAFETY_INFO("cat_shelter_safety_info"),
    CAT_CONTACT_INFO("cat_contact_info"),

    DOG_MEETING_RULES_INFO("dog_meeting_rules_info"),
    DOG_DOCUMENT_LIST("dog_document_list"),
    DOG_TRANSPORTATION_RECOMMENDATION("dog_transportation_recommendation"),
    DOG_YOUNG_HOUSING_RECOMMENDATION("dog_young_housing_recommendation"),
    DOG_HOUSING_RECOMMENDATION("dog_housing_recommendation"),
    DOG_DISABLED_HOUSING_RECOMMENDATION("dog_disabled_housing_recommendation"),
    DOG_DECLINE_CAUSES("dog_cynologist_recommendation"),

    DOG_CYNOLOGIST_ADVICES("dog_cynologist_advices"),
    DOG_CYNOLOGIST_RECOMMENDATION("dog_cynologist_recommendation"),

    CAT_MEETING_RULES_INFO("cat_meeting_rules_info"),
    CAT_DOCUMENT_LIST("cat_document_list"),
    CAT_TRANSPORTATION_RECOMMENDATION("cat_transportation_recommendation"),
    CAT_YOUNG_HOUSING_RECOMMENDATION("cat_young_housing_recommendation"),
    CAT_HOUSING_RECOMMENDATION("cat_housing_recommendation"),
    CAT_DISABLED_HOUSING_RECOMMENDATION("cat_disabled_housing_recommendation"),
    CAT_DECLINE_CAUSES("cat_cynologist_recommendation"),
    ;

    private final String name;

    Callbacks(String name) {
        this.name = name;
    }
}
