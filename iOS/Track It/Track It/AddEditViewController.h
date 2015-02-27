//
//  AddEditViewController.h
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "Tasks.h"
#import "Reachability.h"

@interface AddEditViewController : UIViewController{
    
    int priorityLevel;
    IBOutlet UIButton *submitEdit;
    
    Boolean isOnline;
    Boolean goodTitle;
    Boolean goodPriority;
}
@property (strong, nonatomic) IBOutlet UITextField *taskTitle;
@property (strong, nonatomic) IBOutlet UISegmentedControl *taskPriority;
@property (strong, nonatomic) IBOutlet UITextView *taskDesc;
@property (strong, nonatomic) Tasks* currentCell;

- (IBAction)onClick:(id)sender;

@end
