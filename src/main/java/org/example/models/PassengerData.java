package org.example.models;

public class PassengerData {
    private String contactLastName;
    private String contactFirstName;
    private String phone;
    private String email;
    private String accommodationType;
    private String fromStation;
    private String toStation;
    private String travelDate;
    private String trainNumber;
    private String paymentStation;
    private String passengerCategory;
    private String passengerLastName;
    private String passengerFirstName;
    private String idCardNumber;
    private String issueDate;
    private String issuedBy;
    private String documentPath;

    public PassengerData() {}

    // Builder pattern для зручності
    public static class Builder {
        private PassengerData data = new PassengerData();

        public Builder contactLastName(String value) {
            data.contactLastName = value;
            return this;
        }

        public Builder contactFirstName(String value) {
            data.contactFirstName = value;
            return this;
        }

        public Builder phone(String value) {
            data.phone = value;
            return this;
        }

        public Builder email(String value) {
            data.email = value;
            return this;
        }

        public Builder accommodationType(String value) {
            data.accommodationType = value;
            return this;
        }

        public Builder fromStation(String value) {
            data.fromStation = value;
            return this;
        }

        public Builder toStation(String value) {
            data.toStation = value;
            return this;
        }

        public Builder travelDate(String value) {
            data.travelDate = value;
            return this;
        }

        public Builder trainNumber(String value) {
            data. trainNumber = value;
            return this;
        }

        public Builder paymentStation(String value) {
            data.paymentStation = value;
            return this;
        }

        public Builder passengerCategory(String value) {
            data.passengerCategory = value;
            return this;
        }

        public Builder passengerLastName(String value) {
            data.passengerLastName = value;
            return this;
        }

        public Builder passengerFirstName(String value) {
            data.passengerFirstName = value;
            return this;
        }

        public Builder idCardNumber(String value) {
            data.idCardNumber = value;
            return this;
        }

        public Builder issueDate(String value) {
            data.issueDate = value;
            return this;
        }

        public Builder issuedBy(String value) {
            data.issuedBy = value;
            return this;
        }

        public Builder documentPath(String value) {
            data.documentPath = value;
            return this;
        }

        public PassengerData build() {
            return data;
        }
    }

    // Getters
    public String getContactLastName() { return contactLastName; }
    public String getContactFirstName() { return contactFirstName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAccommodationType() { return accommodationType; }
    public String getFromStation() { return fromStation; }
    public String getToStation() { return toStation; }
    public String getTravelDate() { return travelDate; }
    public String getTrainNumber() { return trainNumber; }
    public String getPaymentStation() { return paymentStation; }
    public String getPassengerCategory() { return passengerCategory; }
    public String getPassengerLastName() { return passengerLastName; }
    public String getPassengerFirstName() { return passengerFirstName; }
    public String getIdCardNumber() { return idCardNumber; }
    public String getIssueDate() { return issueDate; }
    public String getIssuedBy() { return issuedBy; }
    public String getDocumentPath() { return documentPath; }
}