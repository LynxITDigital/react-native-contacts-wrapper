# react-native-contacts-wrapper

This is a simple wrapper for the native iOS and Android Contact Picker UIs.  When calling the API functions, the appropriate picker is launched.  If a contact is picked, the promise is resolved with the requested data about the picked contact.

This uses the ContactsContract API for Android, AddressBook library for iOS8 and below and the new Contacts library for ios9+.

The API is currently very basic.  This was started just as a way of selecting a contact's email address.  The getContact function was added as a more generic way of returning contact data.  Currently this returns Name, Phone and Email for picked contact.  In future more fields will be added to this, and possibly more specific methods similar to getEmail.  

Feel free to extend the functionality so it's more useful for everyone - all PRs welcome!

## Installation

### Android
in `settings.gradle`

```
include ':react-native-custom-module'
project(':react-native-custom-module').projectDir = new File(settingsDir, '../node_modules/react-native-custom-module/android/')
```

in `build.gradle`

```
compile project(':react-native-contacts-wrapper')
```

in `Main.java`
add package to getPacakges()
```
@Override
protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        ….
       new ContactsWrapperPackage()
   );
}
```


### iOS

1. Open your xCode project
2. Click project name in project navigator
3. Select the main target in the left hand menu
4. Scroll to Linked Frameworks and Libraries
5. click + at bottom
6. Click ‘Add Other'
7. Find and add your new project’s .xcodeproj file from node_modules
8. This will now appear in project explorer, drag in under the Libraries group.
9. In same screen, click + again, you should now see the .a file for you project, Add this
10. Clean and Rebould your Xcode project


##API

`getContact` (Promise) - returns basic contact data as a JS object.  Currently returns name, first phone number and first email for contact.
`getEmail` (Promise) - returns first email address (if found) for contact as string.


##Usage

Methods should be called from React Native as any other promise.

###Example
```
import ContactsWrapper from 'react-native-contacts-wrapper';
...
ContactsWrapper.getEmail()
        .then((email) => {
        console.log("email is", email);
      })
      .catch((error) => {
        console.log("ERROR CODE: ", error.code);
        console.log("ERROR MESSAGE: ", error.message);
      });
```

