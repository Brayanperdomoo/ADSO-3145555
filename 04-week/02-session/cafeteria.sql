/****************************************************************************************************
* Create a database named `coffee-shop` to store all the data related to the coffee shop operations.*
*                                                                                                   *
* Use SMBD postgreSQL to create the database and ensure that it is properly                         *
* set up for use in subsequent exercises.                                                           *
*                                                                                                   *
* Use docker to run a PostgreSQL container, is required activity. Make sure to follow the           *
instructions provided in the course materials to set up the container correctly.                    *
*****************************************************************************************************/

DROP DATABASE IF EXISTS coffee-shop;

CREATE DATABASE coffee-shop;

USING coffee-shop;

/*****************************************
* atributes audit                        *
* created_at    TIMESTAMPTZ DEFAULT NOW()*
* updated_at    TIMESTAMPTZ              *
* deleted_at    TIMESTAMPTZ              *
* created_by    UUID                     *
* updated_by    UUID                     *
* deleted_by    UUID                     * 
* status        UUID                     *
******************************************/


/****************************************
* tables:                               *
*                                       *
* Module 2: Parameter                   *
* - type_document                       *
* - person                              *
* - file                                *
*                                       *           
* Module 1: Security                    *
* - user                                *
* - role                                *
* - module                              *
* - view                                *
* - user_role                           *
* - role_module                         *
* - module_view                         *
*                                       *
* Module 3: Inventory                   *
* - category                            *
* - product                             *
* - supplier                            *
* - inventory                           *
*                                       *
* Module 4: Sales                       *
* - customer                            *
* - order                               *
* - order_item                          *
*                                       *
* Module 4: Method_pyment               *
* - method_payment                      *
*                                       *
* Module 5: Billing                     *
* - invoice                             *
* - invoice_item                        *
* - payment                             *
*                                       *



