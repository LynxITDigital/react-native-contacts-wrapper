//
//  RCTContactsWrapper.h
//  RCTContactsWrapper
//
//  Created by Oliver Jacobs on 15/06/2016.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "RCTBridgeModule.h"

@import Contacts;
@import ContactsUI;
@import AddressBook;
@import AddressBookUI;

@interface RCTContactsWrapper : NSObject <RCTBridgeModule, CNContactPickerDelegate, ABPeoplePickerNavigationControllerDelegate>




@end
