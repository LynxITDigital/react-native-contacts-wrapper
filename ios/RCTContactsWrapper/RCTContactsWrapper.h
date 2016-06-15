//
//  RCTContactsWrapper.h
//  RCTContactsWrapper
//
//  Created by Oliver Jacobs on 15/06/2016.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "RCTBridgeModule.h"
#import "RCTLog.h"
@import ContactsUI;



@interface RCTContactsWrapper : NSObject <RCTBridgeModule, CNContactPickerDelegate>

@end
