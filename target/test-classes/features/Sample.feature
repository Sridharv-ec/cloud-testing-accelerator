@cloud
Feature: Sample feature file

  Scenario: Fetch sample data and row count from BQ table
    Given Get data from BQ table "Config_table"
    And Get record count for "Query_1" from BQ table

  Scenario Outline: Upload object to GCP  cloud storage bucket
    Then Upload "<FileName>" Object into "<BucketName>" bucket
    Examples:
      | FileName        | BucketName     |
      | Test_data_1.dat | landing-bucket |
      | testdata.json   | landing-bucket |
