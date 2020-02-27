package com.example.government;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Government implements Serializable {
    private String title;
    private String name;
    private  String party;

    //not need to put in recyclerview
    private String pimageurl;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String displayFinaltext;
    List<String> listchannels = new ArrayList<String>();
    //

    public Government(String title, String name, String party, String pimageurl, String address, String phoneNumber, String email, String website, List<String> listchannels, String displayFinaltext) {
        this.title = title;
        this.name = name;
        this.party = party;
        this.pimageurl = pimageurl;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.listchannels = listchannels;
        this.displayFinaltext = displayFinaltext;
    }



    public String getPimageurl() {
        return pimageurl;
    }

    public void setPimageurl(String pimageurl) {
        this.pimageurl = pimageurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getListchannels() {
        return listchannels;
    }

    public void setListchannels(List<String> listchannels) {
        this.listchannels = listchannels;
    }

//recycler needed

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setParty(String party) {
        this.party = party;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getDisplayFinaltext() {
        return displayFinaltext;
    }

    public void setDisplayFinaltext(String displayFinaltext) {
        this.displayFinaltext = displayFinaltext;
    }

    @Override
    public String toString() {
        return "Government{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", pimageurl='" + pimageurl + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", listchannels=" + listchannels +
                '}';
    }
}
