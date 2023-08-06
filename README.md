<!--
*** Thanks for checking out the best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]




<!-- PROJECT LOGO -->
<br />
<p align="center">

  <h3 align="center">About GCP testing accelerator</h3>
  <p align="center">
    BDD testing framework for cloud services
  </p>


<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About GCP testing accelerator</a>
      <ul>
        <li><a href="#built-with">Built with</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About GCP testing accelerator


 
The cloud testing accelerator is an automation framework tused o test Cloud  (GCP) applications, processes and services using open source. We have included all major cloud capabilities and services needed for testing as generic BDD functions .

Any team can clone the accelerator and get started with cloud automation without he need to write code or devote effort in automation setup - this will save time and effort for the team.
 
### Major capabilities provided by tool:
* BDD Based framework - Business-friendly test scenarios which are easy to review and collaborate
* Generic methods – All GCP services and application are exposed as generic functions and all projects specify details setup via config files
* Environment agnostic : Framework is env agnostic i.e. We can run tests on different env without code changes  
* Cucumber reporting Integrated – For easy test execution reporting and result sharing
* Test data management : Test data files are maintained in framework independent json files for easy change and maintenance
* Documentation and setup guide – Framework is accompanied by documentation and setup guide for easy of setup and use

### Built with

This section should list any major frameworks that you built your project with. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.
* [Java](https://www.java.com/en/)
* [GCP](https://cloud.google.com/)
* [Cucumber](https://cucumber.io/)



<!-- GETTING STARTED -->
## Getting started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* Java : Get started by downloading Java -  https://www.java.com/en/download/
* Maven : Install Maven - https://maven.apache.org/install.html

### Installation

1. Clone the repo
   ```sh
   git clone https://pscode.lioncloud.net/psinnersource/quality-engineering/cloud-specific-qe-accelerators/cloud-testing-accelerator
   ```
2. Install project dependencies
   ```sh
   mvn clean install
   ```
3. Update project specific configuration in properties file (src/test/resources/profile/sandbox/environment.properties)
4. To access GCP app locally authenticate using below cmd:
   ```sh
   gcloud auth application-default login
   ```
5. Start all test cases execution by executing below command 
   ```sh
   mvn -Dprofile=profile/sandbox integration-test
   ```
6. Start specify test cases execution by tag using below command 
   ```sh
      mvn -Dprofile=profile/sandbox -Dcucumber.options="--tags @cloud" integration-test
   ```
<!-- USAGE EXAMPLES -->
## Usage

Environment agnostic: create properties file under src/test/resources/profile folder with environment name you want to use in run command i.e for int env create folder named int and use below run commands to fetch all configuration for int env

 ```sh
   mvn -Dprofile=profile/int integration-test
   ```
   
   
### Cucumber steps for different services:

BDD steps for BigTable:
```sh
Create a big table {tableId} with column {column_family}
Read data from big table {tableId}
Write data to big table {tableId} with column {column_family}. — Refactor for data
```
BDD steps for big query:
```
Get data from BQ table {tableName}
Add empty column {string} to dataset {string} and table {string}
Add columns {columnName} to dataset {dataSetName} and table {tableName}
Verify if dataset {dataSetName} exists in BigQuery
Create new dataset {dataSetName}
Create new table with dataset {dataSetName} table {tableName} and schema {schemaFile}
Delete dataset {datasetName}
Delete table {tableName} from dataset {datasetName}
Get dataset information for dataset {datasetName}
Get properties for {tableName} table in {datasetName} dataset
Get total count of rows from BQ table {string}
Get record count for {sql query} from BQ table
Get list of all tables in {datasetName} dataset
Get data from BQ for {string} partitioned table having start_date {string} and end_date {string}
Get data from partitioned BQ table for {sql query} having start_date {start_date} and end_date {end_date}
```
BDD steps for Cloud functions:
```
Cloud functions:
Create proto for schema {schemaId} with fileName {fileName}
```

BDD steps for Cloud logging:
```
Create sink {sinkName} for dataset {datasetName}
Update sink {sinkName} for dataset {datasetName}
List all sink
Delete sink {sinkName}
Delete log {logName}
List log {log filter}
```

BDD steps for Cloud Monitoring:
```
List all alert policies for the project
Get all alert policies
Enable policy for filter {filter}
```
BDD steps for Cloud spanner:
```
Create spanner DB {DatabaseId} for SQL {query}
Query spanner DB and fetch {fieldName} using SQL {query}
Insert into spanner DB using {query}
Update data in spanner DB using {query}
Delete data from spanner DB using {query}
```
BDD steps for Cloud storage:
```
Copy object {objectName} from {sourceBucketName} to {targetBucketName}
Create bucket {bucketName}
Delete bucket {bucketName}
Upload {objectName} Object into {bucketName} bucket
```
BDD steps for Cloud SQL -
```
Create cloud MYSQL table using {sql query}
Create cloud PostGres table using {sql query}
Create cloud SQL Sever table using {sql query}
Drop cloud MYSQL table {TableName}
Drop cloud PostGres table {TableName}
Drop cloud SQL Sever table {TableName}
Get records from cloud MYSQL table using {sql query}
Get records from cloud PostGres table using {sql query}
Get records from cloud SQL Sever table using {sql query}
```
BDD steps for Data proc:
```
Create data proc cluster {clusterName} with autoscaling policy {autoscalingPolicyName}
Create data proc cluster {clusterName}
Submit data proc job for cluster {clusterName}
```
BDD steps for Firestore
```
Get document result as map for collection {collectionName} document {documentName}
Get query result for collection {collectionName} document {documentName}
Get all records for collection {collectionName}
```
BDD steps for PubSub:
```
Create pull subscription for topic {topicId} with subscriptionId {subscriptionId}
Create push subscription for topic {topicId} with subscriptionId {subscriptionId}
Create push subscription at {pushEndpoint} endpoint for topic {topicId} with subscriptionId {subscriptionId}
Create topic {topicId}
Delete topic {topicId}
Delete subscription with subscriptionId {subscriptionId}
Detach subscription with subscriptionId {subscriptionId}
Get subscription policy for subscriptionId {subscriptionId}
Get Subscription list of the project
Get Subscription list for topic {topicId}
```
BDD steps for Secret Manager:
```
Get secret {secretId}
Create secret {secretId}
Grant member {member} access to {secretId}
List all secret for the project
```



<!-- ROADMAP -->
## Roadmap



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Prashant Kumar - prashant.kumar@publicissapient.com




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/othneildrew/Best-README-Template/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/othneildrew/Best-README-Template/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/othneildrew/Best-README-Template/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/othneildrew/Best-README-Template/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/othneildrew/Best-README-Template/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
[product-screenshot]: images/screenshot.png
