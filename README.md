# Health Tracker Rest API
__Name:__ Karthikeyan Gopal

## Overview

> This Application consists wide variety of API to accomplish create, update, read and delete operations on various endpoints. They are
> + Users
> + Activities
> + Measurements
> + Nutritions
> + UserCredentials
> + Nutrient Search on External API (EDAMAN)

## Installation
> mvn clean install

##  OpenAPI Specifications
[OpenAPI Speicifications](relative/HealthTrackerOpenAPI.yaml)

## Database
> API based postgresSql database is used for all backend storage.

## API Service
> Javalin API Builder is used for all routing.

## Error Handling
> The API uses standard HTTP status codes for indicating the success or failure of a request
> All the API's generally respond with 200 series status for any successful responses. And 400 series for failures. 

## Users
> Users end point provides 6 services
> + Get All Users
> + Add User
> + Update User
> + Delete user
> + Get User by User Id
> + Get User by email id
> 
> User schema has 
> + id
> + name
> + email
> + gender
> + birthDate
> + mobileNumber
> + dietPreferences
> + height
> + weight
> + profession

## Activities
> Activities endpoint provides 7 services
> + Get all activities
> + Add activity
> + Update activity by activity id
> + Delete activity by activity id
> + Get activity by activity id
> + Get activities by User id within date period
> + Delete all activities that belong to a User
> 
> Activity schema includes
> + id
> + description
> + duration
> + calories
> + started
> + userId

## Measurements
> Measurements endpoint provides 7 services
> + Get all Measurements
> + Add Measurement
> + Update Measurement by Measurement id
> + Delete Measurement by Measurement id
> + Get Measurement by Measurement id
> + Get Measurements by User id within date period
> + Delete all Measurements that belong to a User
>
> Measurement schema includes
> + id
> + weight
> + chest
> + bicep
> + neck
> + abdomen
> + waist
> + lowerWaist
> + thigh
> + calves
> + measuredDate
> + userId

## Nutrient Search through Edaman API
> This search API consumes an external Food database API provided by Edaman. This is using a free tier account to get
> certain nutrient details for given food name. This might not be accurate. But it gives users an idea and they can adjust.
> 
> This endpoint provide 1 service.
> + Get Nutrient for given food name.
> 
> Nutrient Schema includes,
> + Food Name
> + Calories
> + Protein
> + Cholesterol
> + Fat
> + Fiber

## Nutritions
> Nutritions endpoint provides 7 services
> + Get all Nutritions
> + Add Nutritions
> + Update Nutrition by Nutrition id
> + Delete Nutrition by Nutrition id
> + Get Nutrition by Nutrition id
> + Get Nutritions by User id within date period
> + Delete all Nutritions that belong to a User
>
> Nutrition schema includes
> + id
> + partOfDay
> + foodName
> + calories
> + cholesterol
> + protein
> + fat
> + fiber
> + macroDate
> + userId

## User Credentials
> User Credential endpoint provides 3 services.
> Since this endpoint is handling with passwords, all passwords and base64 encoded during transit and while stored
> in the database.
> 
> + Get UserCredential
> + Add UserCredential
> + Delete UserCredential
> 
> UserCredential Schema includes
> + id
> + password
> + userId

## Conclusion
> This concludes the Health Tracker API documentation.

