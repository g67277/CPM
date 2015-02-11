//
//  AddEditViewController.h
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface AddEditViewController : UIViewController{
    
    int priorityLevel;
}
@property (strong, nonatomic) IBOutlet UITextField *taskTitle;
@property (strong, nonatomic) IBOutlet UISegmentedControl *taskPriority;
@property (strong, nonatomic) IBOutlet UITextView *taskDesc;


- (IBAction)onClick:(id)sender;

@end
