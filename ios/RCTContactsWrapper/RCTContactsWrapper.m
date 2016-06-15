//
//  RCTContactsWrapper.m
//  RCTContactsWrapper
//
//  Created by Oliver Jacobs on 15/06/2016.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "RCTContactsWrapper.h"
@import Contacts;
@import ContactsUI;



@implementation RCTContactsWrapper

RCTPromiseResolveBlock _resolve;
RCTPromiseRejectBlock _reject;


RCT_EXPORT_MODULE(ContactsWrapper);


RCT_EXPORT_METHOD(getEmail:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
  {
    _resolve = resolve;
    _reject = reject;
    CNContactStore *contactStore = [[CNContactStore alloc] init];
    CNContactPickerViewController *picker = [[CNContactPickerViewController alloc] init];
    picker.delegate = self;
    UIViewController *root = [[[UIApplication sharedApplication] delegate] window].rootViewController;
    [root presentViewController:picker animated:YES completion:nil];
    
    
    
  }


#pragma mark - Event handlers

- (void)contactPicker:(CNContactPickerViewController *)picker didSelectContact:(CNContact *)contact {
  if([contact.emailAddresses count] < 1) {
    _reject(@"E_CONTACT_NO_EMAIL", @"No email found for contact", nil);
    return;
  }
  
  CNLabeledValue *email = contact.emailAddresses[0];
  _resolve(contact.emailAddresses[0].value);
  return;
  
}


- (void)contactPickerDidCancel:(CNContactPickerViewController *)picker {
  _reject(@"E_CONTACT_CANCELLED", @"Cancelled", nil);
  return;
}

@end
