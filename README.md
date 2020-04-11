# Schedule-me
Schedule-me is a simple android application to manage your day to day schedules.The application uses firebase to store the data as well for the user authentication. The application is still in development  as there are many features to be implemented and bugs to be fixed.

 

# Features currently implemented

  -  User creation and authentication through firebase
  -  Add , view, edit, remove schedules .

# Features to be implemented
  - Notify the user earlier(notify before a specific time) when a schedule is gonna take place
  - Mark schedules as done and remove them after some time automatically.
  - Forgot password option
 
### Current build 

Download the app to check workflow [Google Drive](https://drive.google.com/open?id=1Dbp4og8fcPd_CNtTEP6vdvzowLQrQPaw)



Setup steps
===========

 1. First of all you need google-services.json(removed due to security purpose). Create a Firebase project in the [Firebase console](https://console.firebase.google.com/), if you don't already have one. Go to your project and click ‘Add Firebase to your Android app’. Follow the setup steps. At the end, you'll download a google-services.json file which you should add to your project.

 ![google_service_json](https://user-images.githubusercontent.com/7821425/32899277-30da3374-caf3-11e7-86e0-58cb1bfd59e2.png)

 2. Setup realtime database. In firebase console go to DEVELOP->Database-> Get Started -> choose tab ‘RULES’ and paste this:

 ```
 {
   "rules": {
     ".read": "true",
     ".write": "true"
   }
 }
 ```
 
3. Go to authentication and enable signin method 
      Now you can run the app successfully

 

# Modules
#### 1. Create Account
   

  ![create-account](https://user-images.githubusercontent.com/42109385/79038040-d9d36280-7bf3-11ea-9bb7-7a94ef13cc4f.jpg)

### 2. User authentication

![sign-in](https://user-images.githubusercontent.com/42109385/79038097-536b5080-7bf4-11ea-8aec-18f449d7526f.jpg)

### 3. Adding schedules

![submit-schdl](https://user-images.githubusercontent.com/42109385/79038360-4fd8c900-7bf6-11ea-9dd0-e6d7d4d1bc96.gif)
### 4. View & edit schedules

![viewedt-schdl](https://user-images.githubusercontent.com/42109385/79038594-1c973980-7bf8-11ea-8059-a0dd999204bc.gif)



 
  


### Contribute
Feel free to contribute on this project for the features that are mentioned to be implemented or for any bug fix. If you have any new feature in mind then please first create a new issue and discuss the idea or reach me at saptarshii.das@gmail.com



License
----

Apache 2.0




[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [Bubble Navigation]: <https://github.com/gauravk95/bubble-navigation>
   [Any Chart]: <https://github.com/AnyChart/AnyChart-Android>

  


