# Society Manager

## Contents

1. [Introduction](#Introduction)
2. [Basic Setup](#basic-setup)
3. **Requests**
   1. [TENANT](#tenant)
       1. [Create Tenant](#create-tenant)
       2. [Update Tenant](#update-tenant)
       3. [Delete Tenant](#delete-tenant)   
       4. [View Specific Tenant](#view-specific-tenant)
       5. [View All Tenants](#view-all-tenants)
   2. [Payment](#payment)
       1. [Create Payment](#create-payment) 
       2. [Update Payment](#update-payment)
       3. [View All Payments](#view-all-payments)
       4. [View All Payments For A Tenant](#view-payments-for-a-tenant)
   3. [Complain](#complain)
   	   1. [Create Complain](#create-complain)
   	   2. [Update Complain](#update-complain)
      3. [View All Complains](#view-all-complains)
      4. [View All Complains For A Tenant](#view-complains-for-a-tenant)
   4. [Feedback](#feedback)
      1. [Create Feedback](#create-feedback)
      2. [Update Complain](#update-complain)
      3. [View All Payments](#view-all-complains)
      4. [View All Payments For A Tenant](#view-complains-for-a-tenant)
4. [Notes](#note)

## Introduction

## Basic Setup

Pull latest code from master branch and build using maven.

Here are the Events implemented in Core Person and how to use them.
Database : PostGres . Configured to run on docker inside a container. Pull the
		   latest image from docker registry and run it using 'docker-compose' up
		   on the port number 5432. To make custom changes, change 		                              		   'application-docker.properties' file under resources
		   
**Base Path :**

         -Localhost : Port 9090
         -CloudFoundry: https://societymanager.cfapps.us30.hana.ondemand.com/


## Tenant

Tenant is a person who can buy , rent , lease a certain property(s) for a certain time 
or purchase it altogether.

**Tenant has :**
      
      1. Own Details
      2. Occupancy Details
      3. Payment Details
      4. Feedback/Complain

 There are different requests associated with tenants.



### Create Tenant
Create A Tenant Using a Unique Registration / User Id

**URL:** http://localhost:9090/CreateTenant

**Method:** POST

**Sample Request Body:**

```
{
    "registrationId": "R901",
    "tenantName": "Raffael Wick",
    "permanentAddress": "Lakeview Avenue",
    "password" : "76543wq",
    "contactNumber": 3214561456,
    "countryCode": "IND",
    "dateOfBirth": "2010-10-19"
}

```


### Update Tenant
Update A Tenant Using a Unique Registration / User Id

**URL:** http://localhost:9090/UpdateTenant

**Method:** PUT

**Sample Request Body:**

```
{
    "registrationId": "R901",
    "uuid" : "{{TenantUuid}}",
    "tenantName" : "Cody Wick Wooten",
    "dateOfBirth" : "2000-08-14",
    "password" : "76543wq",
    "permanentAddress" : "Lakeview Avenue",
    "countryCode": "IND",
    "contactNumber" : 321456
}

```


### Delete Tenant
Create A Tenant Using a UUID

**URL:** http://localhost:9090/DeleteTenant

**Method:** DELETE

**Sample Request Body:**

```
{
   "tenantUuid" : "{{tenantUuid}}",
   "messageCode" : "Test deleted"
}
```


### View Specific Tenant

View Details Of a Specific Tenant Using Registration Id

**URL:** http://localhost:9090/TenantViews/{{tenantId}}

**Method:** GET




### View All Tenants
View List OF All Tenant

**URL:** http://localhost:9090/TenantViewsAll

**Method:** GET

## Payment
Payment created for each type of property occupancy (will be updated soon).

### Create Payment
Create A Payment Using Tenant UUID

**URL:** http://localhost:9090/CreatePayment

**Method:** POST

**Sample Request Body:**

```
{
    "tenantUuid" : "{{tenantUuid}}",
    "paymentType" : "Cash",
    "paymentAmount" : "1240",
    "paymentDate" : "2019-02-16"
}

```
Save the uuid received in response as Payment Uuid.

### Update Payment
Update a Specific Payment Detail created for a Tenant

**URL:** http://localhost:9090/UpdatePayment

**Method:** POST

**Sample Request Body:**

```
{
    "uuid" : "{{paymentUuid}}"
    "tenantUuid" : "{{tenantUuid}}",
    "paymentType" : "Cash+Card+Check",
    "paymentAmount" : "1240",
    "paymentDate" : "2019-02-16"
}

```


### View All Payments
List out all the payments for all tenants

**URL:** http://localhost:9090/PaymentViewsAll

**Method:** GET

### View Payments For A Tenant
List out all the payments for a specific tenant

**URL:** http://localhost:9090/PaymentViews/{{tenantUuid}}

**Method:** GET

## Complain

### Create Complain
Create A Complain Using Tenant UUID

**URL:** http://localhost:9090/CreateComplain

**Method:** POST

**Sample Request Body:**

```
{
    "tenantUuid" : "{{tenantUuid}}",
    "complainType" : "Housekeeping",
    "priority" : "LOW",
    "messageDetails" : "Room Cleaning is not regular",
    "complainDate" : "2019-02-06"
}

```
in response uuid returned is the complainUuid.

### Update Complain
Update A Complain Using previous Complain UUID

**URL:** http://localhost:9090/UpdateComplain

**Method:** POST

**Sample Request Body:**

```
{
	"complainUuid" : "2b675f16-7712-4800-b8c1-5c8a541ff7b4",
    "tenantUuid" : "{{tenantUuid}}",
    "complainType" : "Housekeeping",
    "priority" : "Medium",
    "messageDetails" : "Room Cleaning is not regular for last few weeks",
    "complainDate" : "2019-02-22"
}

```

### View All Complains
List out all the complains for all tenants

**URL:** http://localhost:9090/ComplainViewsAll

**Method:** GET

### View Complains For A Tenant
List out all the payments for a specific tenant

**URL:** http://localhost:9090/ComplainViews/{{tenantUuid}}

## Feedback

### Create Feedback
Create A Feedback Using Tenant UUID

**URL:** http://localhost:9090/CreateFeedback

**Method:** POST

**Sample Request Body:**

```
{
    "tenantUuid" : "{{tenantUuid}}",
    "rating" : 4,
    "suggestion" : "24/7 water supply",
    "appreciationMessage" : "posh and calm neighbourhood",
    "feedbackDate" : "2019-02-16"
}

```

in response uuid returned is the feedbackUuid.

### Update Feedback
Update A Feedback Using previous Feedback UUID

**URL:** http://localhost:9090/UpdateFeedback

**Method:** POST

**Sample Request Body:**

```
{
    "tenantUuid" : "{{tenantUuid}}",
    "feedbackUuid" : "419d22cb-3bc9-402d-b9b0-54e9e46e33d9",
    "rating" : 4.7,
    "suggestion" : "24/7 water supply",
    "appreciationMessage" : "posh and calm neighbourhood,fast resolution of issues",
    "feedbackDate" : "2019-02-16"
}

```

### View All Feedbacks
List out all the feedbacks for all tenants

**URL:** http://localhost:9090/FeedbackViewsAll

**Method:** GET

### View Feedbacks For A Tenant
List out all the payments for a specific tenant

**URL:** http://localhost:9090/FeedbackViews/{{tenantUuid}}


## Note
1. Save The Response UUID of Tenant which will be used for later payment, feedback,complain CRUD operation
2. Registration ID unique and only used by the tenant, internally for any operation we use UUID of tenant                	that is sent back as response body.
3. Basic CRUD operations for all the entities are working Fine !!
4. This application records all the changes to the Business Object After Every Transactions
5. UUID represents each entity created (for example: Payment, Feedback, Complain), any changes made
   to those entities are tracked through row ID.

  > IMPROVEMENT : Create Entity Payment Type which records rent/cost, payment frequency, payment date, advance (lease, caution money etc.) and other fields (fine amount, late charge)
