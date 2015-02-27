//
//  SignUpViewController.h
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import "MainViewController.h"
#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "Reachability.h"
@class MainViewController;

@interface SignUpInViewController : UIViewController{
    
    Boolean goodEmail;
    Boolean goodUsername;
    Boolean goodPassword;
    Boolean isOnline;
}

@property (strong, nonatomic) IBOutlet UITextField *signUpEmail;
@property (strong, nonatomic) IBOutlet UITextField *signUpUsername;
@property (strong, nonatomic) IBOutlet UITextField *signUpPass;
@property (strong, nonatomic) IBOutlet UITextField *signInUsername;
@property (strong, nonatomic) IBOutlet UITextField *signInPass;
@property (strong, nonatomic) IBOutlet UITextField *resetPassEmail;

-(IBAction)onSignUP:(id)sender;
-(IBAction)onSignIn:(id)sender;
-(IBAction)onReset:(id)sender;

@end
