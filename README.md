SENDOTP APP

Firstly, I create a fakeJsonData Class which contain the fake contact details in json format. If want to add more contacts then we can add in that file. Currently this file contain random mobile numbers. Please change numbers in fakeJson class for correct working of this app.
SavaData, In this class we create a table in database. 
When app opens, then home activity open which include two tabs with the help of fragments. First one is contact list and second one is message list tab.
Contact class is shown in the first tab. Contact tab show the list of all contacts added in fakeJsondata class. We use the Json Object for getting the data from fakejsondata class and show the Contact name data in the listview. When we click on the any item of the list. Firstly, it check the contact number length. If length i s less than 10 then it shows toast showing “Invalid Number” else go to next Activity SendMessage using Intent.
Message List Tab shows the list of OTP messages send by the app sorted in descending order of time. I f there is no OTP send then it show blank screen with message NO OTP IS SENT. We fetch the details of send otp messages from database.
In sendMessage class, Activity show the details of selected contact. It also contain a button. On pressing of this button go to next s=activity OTP by using Intent.
In OTP class contain a text view which show the otp that is going to s=be send and a button. On the click of button OTP is sent.  We use the MSG91 API to send the OTP message and REST API to execute the API. Json object is used to get data. In this activity we also check the network connectivity. In internet is disabled, then display toast with no internet connection. otherwise, It send the otp and then save the details in database along with the time. 
In manifest file, we add the network connectivity connection and network state permission, so that we can access both in our app. In app, if we send the message, then the details of send message is shown in message list tab next time we open the app.



