package dwayne.shim.geogigani.common.data;

import lombok.Data;

@Data
public class TravelData {

    //***********************************************************************
    // Basic Information
    //***********************************************************************
    private String addr1;
    private String addr2;

    private String areaCode;

    private String cat1;
    private String cat2;
    private String cat3;

    private String contentId;
    private String contentTypeId;

    private long createdTime;
    private long modifiedTime;

    private String firstImage;
    private String firstImage2;

    private double mapX;
    private double mapY;
    private int mLevel;

    private long readCount;
    private String siGunGuCode;
    private String tel;
    private String title;
    private String zipCode;

    //***********************************************************************
    // Common Information
    //***********************************************************************
    private String bookTour;
    private String homePage;
    private String overview;

    //***********************************************************************
    // Detailed Information
    //***********************************************************************
    private String ageLimit;
    private String bookingPlace;

    private String spendTimeFestival;
    private String useTimeFestival;
    private String discountInfoFestival;

    private long eventStartDate;
    private long eventEndDate;
    private String eventPlace;
    private String placeInfo;
    private String playTime;
    private String program;

    private String sponsor1;
    private String sponsor1Tel;
    private String sponsor2;
    private String sponsor2Tel;

    private String subEvent;
}
