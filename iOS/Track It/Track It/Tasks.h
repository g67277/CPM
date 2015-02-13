//
//  Tasks.h
//  Track It
//
//  Created by Nazir Shuqair on 2/10/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Tasks : NSObject

@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* tdescription;
@property (nonatomic, strong) NSString* objectID;
@property (nonatomic) int priority;

@end
