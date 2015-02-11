//
//  AddEditViewController.m
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import "AddEditViewController.h"
#import "MainViewController.h"

@interface AddEditViewController ()

@end

@implementation AddEditViewController
@synthesize  taskTitle, taskDesc, taskPriority;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(UIButton*)sender{
    if (sender.tag == 0) {
        if (taskPriority.selectedSegmentIndex == 0) {
            priorityLevel = 1;
        }else if (taskPriority.selectedSegmentIndex == 1) {
            priorityLevel = 2;
        }else if (taskPriority.selectedSegmentIndex == 2) {
            priorityLevel = 3;
        }else if (taskPriority.selectedSegmentIndex == 3) {
            priorityLevel = 4;
        }
    }else if (sender.tag == 1){
        [self saveTask];
    }
}

- (void) saveTask{
    
    PFACL *defaultACL = [PFACL ACLWithUser:[PFUser currentUser]];
    [PFACL setDefaultACL:defaultACL withAccessForCurrentUser:YES];
    
    PFObject *task = [PFObject objectWithClassName:@"Tasks"];
    task[@"title"] = taskTitle.text;
    task[@"description"] = taskDesc.text;
    task[@"priority"] = @(priorityLevel);
    [task saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (succeeded) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Saved Successfully"
                                                            message:nil
                                                           delegate:self
                                                  cancelButtonTitle:@"OK"
                                                 otherButtonTitles:nil];
            [alert show];
        } else {
            NSLog(@"%@",error.userInfo);
        }
    }];
}

#pragma mark - Alert view delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:
(NSInteger)buttonIndex{
    switch (buttonIndex) {
        case 0:
            [self.navigationController popViewControllerAnimated:YES];
            break;
        default:
            break;
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
