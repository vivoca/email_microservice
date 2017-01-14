# Email [Microservice](https://en.wikipedia.org/wiki/Microservices)

## Description

##### Email might be one of the most often overlooked pieces of any web application. Usually the biggest discussion around it in a project begins and ends with “and we’ll send them an email when this happens…”.

## Usage

1. Fill the `connection.properties` file with your datbase's properties, use [Postgres](https://newrelic.com/plugins/enterprisedb-corporation/30?_bt=172087290907&_bk=postgres&_bm=p&_bn=g&gclid=CjwKEAiAtefDBRDTnbDnvM735xISJABlvGOv45VXys6R0ILZv6ij2g23EZrr42cie_J4twvp0sHvohoCczLw_wcB)
2. Run `Main` in root folder
3. Run `controller\CreateClient\ClientMaker.java` 
4. Send a post with [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop) in JSON, like this: 

    Request body [JSON](http://www.w3schools.com/js/js_json_intro.asp) format:
        {
            "to":"email address",
            "from":"email adress",
            "message": "It's working!",
            "subject": "Newest",
            "APIKey": "APIKey(you get it from us)"
        }

Obliviously you have to change the `value` in JSON (to, from, APIKey). APIKey is generated, so you can find it in `client` table in your database.


## Some Details of Code

### Database

The microservice creates two table (email, client). If you want to use this service, you have to choose a header and a footer image for your email.

### Request

For instance: 
Possessed an online shop your user want to pay and get an email of the details of shopping.
When user ended this process, he push the pay button. This movement indicates our microservice on the background. The button has to send a JSON (above mentioned). 

If the microservice get every required value in form of request.body(), it saves this data into email table, and sends a response with true for the online shop. 
On the background, the service checked the email table and if there are a status of 'NEW', then run the emailsender() method.
 



