//
//  RCTContactsWrapper.m
//  RCTContactsWrapper
//
//  Created by Oliver Jacobs on 15/06/2016.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "RCTContactsWrapper.h"


@implementation RCTContactsWrapper

RCTPromiseResolveBlock _resolve;
RCTPromiseRejectBlock _reject;


RCT_EXPORT_MODULE(ContactsWrapper);


RCT_EXPORT_METHOD(getEmail:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
  {
    _resolve = resolve;
    _reject = reject;
    UIViewController *picker;
    if([CNContactPickerViewController class]) {
      picker = [[CNContactPickerViewController alloc] init];
      ((CNContactPickerViewController *)picker).delegate = self;
    } else {
      picker = [[ABPeoplePickerNavigationController alloc] init];
      [((ABPeoplePickerNavigationController *)picker) setPeoplePickerDelegate:self];
    }
    UIViewController *root = [[[UIApplication sharedApplication] delegate] window].rootViewController;
    [root presentViewController:picker animated:YES completion:nil];
    
    
    
  }


#pragma mark - Event handlers

- (void)pickerCancelled {
  _reject(@"E_CONTACT_CANCELLED", @"Cancelled", nil);
}

- (void)pickerNoEmail {
  _reject(@"E_CONTACT_NO_EMAIL", @"No email found for contact", nil);
}

-(void)emailPicked:(NSString *)email {
  _resolve(email);
}


- (void)contactPicker:(CNContactPickerViewController *)picker didSelectContact:(CNContact *)contact {
  if([contact.emailAddresses count] < 1) {
    [self pickerNoEmail];
    return;
  }
  
  CNLabeledValue *email = contact.emailAddresses[0].value;
  [self emailPicked:email];
  
}


- (void)contactPickerDidCancel:(CNContactPickerViewController *)picker {
  [self pickerCancelled];
}



- (void)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker didSelectPerson:(ABRecordRef)person {
  ABMultiValueRef emailMultiValue = ABRecordCopyValue(person, kABPersonEmailProperty);
  NSArray *emailAddresses = (__bridge NSArray *)ABMultiValueCopyArrayOfAllValues(emailMultiValue);
  if([emailAddresses count] < 1) {
    [self pickerNoEmail];
     return;
  }
  
  [self emailPicked:emailAddresses[0]];
  
}

- (void)peoplePickerNavigationControllerDidCancel:(ABPeoplePickerNavigationController *)peoplePicker {
  [self pickerCancelled];
}






@end
