The application connects to the Microsoft Graph and fetches the basic user information.

For this application to work one needs to first setup a Microsoft Azure account and create a new application on the Azure portal that would provide as the starting point.
The steps to create and setup an application on Azure portal can be read here .. https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app

The application takes the following steps to fetch the information from the Microsoft Graph API

 1. Run the application
 2. Browse localhost:8080 (assuming you are calling from your local machine)
 3. Fill in the mandatory 'Unique Name' field and Submit [The entered value works as a key to a map that stores necessary information required to connect to Microsoft Graph]
 4. Call token api from command line -> curl localhost:8080/token?user=<Unique Name> [This api will receive the token required for fetching information from Microsoft Graph]
 5. Call fetch api from command line -> curl localhost:8080/fetch?user=<Unique Name> [This api will fetch the information based on the permissions granted to the Azure application]
 
Things to take care before starting the app
1. Complete the registration process
2. Copy the msgraph.properties file to some external location and update that location in application.properties file
3. Change the values in the msgraph.properties file from Azure Portal. All values are attained during the registration process