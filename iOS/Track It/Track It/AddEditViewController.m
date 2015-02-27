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
    priorityLevel = 1;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reachabilityDidChange) name:kReachabilityChangedNotification object:nil];
    
    [self reachabilityDidChange];
    
    if (self.currentCell != nil) {
        self.navigationItem.title = self.currentCell.title;
        submitEdit.tag = 2;
        [submitEdit setTitle:@"Update" forState:UIControlStateNormal];
        [self updateDetails];
    }else{
        self.navigationItem.title = @"Track";
        submitEdit.tag = 1;
        [submitEdit setTitle:@"Track" forState:UIControlStateNormal];
    }
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) updateDetails{
    
    taskTitle.text = self.currentCell.title;
    taskDesc.text = self.currentCell.tdescription;
    switch (self.currentCell.priority) {
        case 1:
            taskPriority.selectedSegmentIndex = 0;
            break;
        case 2:
            taskPriority.selectedSegmentIndex = 1;
            break;
        case 3:
            taskPriority.selectedSegmentIndex = 2;
            break;
        case 4:
            taskPriority.selectedSegmentIndex = 3;
            break;
        default:
            break;
    }
    
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
        if (isOnline) {
            [self saveTask];
        }else{
            [self alertMethod:@"Offline" message:@"Please make sure you're connected and try again"];
        }
    }else if (sender.tag == 2){
        if (isOnline) {
            [self updateTask];
        }else{
            [self alertMethod:@"Offline" message:@"Please make sure you're connected and try again"];
        }
    }
}

- (void) saveTask{
    
    if (taskTitle.text.length < 1) {
        taskTitle.placeholder = @"Required";
        goodTitle = false;
    }else{
        goodTitle = true;
    }
    
    if (goodTitle) {
        PFACL *defaultACL = [PFACL ACLWithUser:[PFUser currentUser]];
        [PFACL setDefaultACL:defaultACL withAccessForCurrentUser:YES];
        
        PFObject *task = [PFObject objectWithClassName:@"Tasks"];
        task[@"title"] = taskTitle.text;
        task[@"description"] = taskDesc.text;
        task[@"priority"] = @(priorityLevel);
        [task saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if (succeeded) {
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Saved!"
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
}

- (void) updateTask{
    
    if (taskTitle.text.length < 1) {
        taskTitle.placeholder = @"Required";
        goodTitle = false;
    }else{
        goodTitle = true;
    }
    
    if (goodTitle) {
        PFQuery *query = [PFQuery queryWithClassName:@"Tasks"];
        
        // Retrieve the object by id
        [query getObjectInBackgroundWithId:self.currentCell.objectID block:^(PFObject *task, NSError *error) {
            
            if (error == nil) {
                task[@"title"] = taskTitle.text;
                task[@"description"] = taskDesc.text;
                task[@"priority"] = @(priorityLevel);
                [task saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                    if (succeeded) {
                        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Updated!"
                                                                        message:nil
                                                                       delegate:nil
                                                              cancelButtonTitle:@"OK"
                                                              otherButtonTitles:nil];
                        [alert show];
                    }else{
                        NSLog(@"%@",error.userInfo);
                    }
                }];
            }else{
                NSLog(@"%@",error.userInfo);
            }
        }];
    }
}

- (void)reachabilityDidChange {
    
    Reachability *reach = [Reachability reachabilityForInternetConnection];
    if ([reach isReachable]){
        isOnline = true;
        NSLog(@"Reachable");
    }else{
        isOnline = false;
        [self alertMethod:@"Offline" message:@"Please make sure you're connected and try again"];
        NSLog(@"Unreachable");
    }
}

- (void) alertMethod:(NSString*) mtitle message:(NSString*) message{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:mtitle
                                                    message:message
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
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
