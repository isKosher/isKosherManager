package com.kosher.iskosher.common.enums;

public enum PopularFoodType {
    PIZZA("פיצה"),
    SUSHI("סושי"),
    SHAWARMA("שווארמה"),
    BURGER("המבורגר"),
    FALAFEL("פלאפל"),
    TOAST("טוסט"),
    ICE_CREAM("גלידה"),
    HUMMUS("חומוס"),
    SALAD("סלט"),
    SOUP("מרק"),
    PASTA("פסטה"),
    STEAK("סטייק"),
    SANDWICH("סנדוויץ'"),
    WAFFLE("וופל"),
    CHICKEN("עוף"),
    FISH("דגים"),
    KEBAB("קבב");

    private final String hebrewName;

    PopularFoodType(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public static PopularFoodType fromHebrew(String hebrewName) {
        for (PopularFoodType type : PopularFoodType.values()) {
            if (type.getHebrewName().equals(hebrewName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid food type: " + hebrewName);
    }
}
