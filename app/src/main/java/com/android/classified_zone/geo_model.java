package com.android.classified_zone;

public class geo_model
{
  String Geo_Name,Radius,latitude,longitude, Purok, Description, disease_name, disease_description, alert_message, name, plat1;
    public String imageURL;
  /*

  Geo_Name
  Radius
  latitude
  longitude
   */

    geo_model()
    {

    }
    public geo_model(String Geo_Name, String name, String  Radius, String   latitude, String longitude, String url, String Description, String Purok, String
            disease_name, String disease_description, String alert_message, String plat1) {
        this.Geo_Name = Geo_Name;
        this.name = name;
        this.Radius = Radius;
        this.latitude= latitude;
        this.longitude = longitude;
        this.imageURL= url;
        this.Description = Description;
        this.Purok= Purok;
        this.disease_description=disease_description;
        this.disease_name=disease_name;
        this.alert_message=alert_message;

        this.plat1 = plat1;
    }

    public String getGeo_Name() {
        return Geo_Name;
    }

    public void setGeo_Name(String Geo_Name) {
        this.Geo_Name = Geo_Name;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name =name;
    }

    public String getRadius() {
        return Radius;
    }

    public void setRadius(String Radius) {
        this.Radius = Radius;
    }

    public String getlatitude() {
        return latitude;
    }

    public void setlatitude(String latitude) {
        this.latitude= latitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getimageURL() {
        return imageURL;
    }

    public void setimageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getPurok() {
        return Purok;
    }

    public void setPurok(String Purok) {
        this.Purok = Purok;
    }

    public String getdisease_name() {
        return disease_name;
    }

    public void setdisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public String getdisease_description() {
        return disease_description;
    }

    public void setdisease_description(String disease_description) {
        this.disease_description = disease_description;
    }
    public String getalert_message() {
        return alert_message;
    }

    public void setalert_message(String alert_message) {
        this.alert_message = alert_message;
    }




}
