//
//  SignUpViewController.m
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import "SignUpInViewController.h"
#import "MainViewController.h"

@interface SignUpInViewController ()

@end

@implementation SignUpInViewController
@synthesize signUpEmail, signUpPass, signUpUsername, signInPass, signInUsername, resetPassEmail;



- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(IBAction)onSignUP:(id)sender{
    
    if (signUpEmail.text.length < 1) {
        signUpEmail.placeholder = @"Required";
        goodEmail = false;
    }else{
        goodEmail = true;
    };
    if (signUpUsername.text.length < 1) {
        signUpUsername.placeholder = @"Required";
        goodUsername = false;
    }else{
        goodUsername = true;
    };
    if (signUpPass.text.length < 1) {
        signUpPass.placeholder = @"Required";
        goodPassword = false;
    }else if(signUpPass.text.length < 4){
        [self alertMethod:@"Too Short" message:@"Password needs to be at least 4 characters"];
        goodPassword = false;
    }else{
        goodPassword = true;
    }
    
    if (goodPassword && goodUsername && goodEmail) {
        PFUser *user = [PFUser user];
        user.email = signUpEmail.text;
        user.username = signUpUsername.text;
        user.password = signUpPass.text;
        
        [user signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if (!error) {
                //move to next screen
                NSUserDefaults* userDefaults = [[NSUserDefaults alloc] init];
                [userDefaults setBool:true forKey:@"signedUp"];
                [self successful];
            }else{
                [self errorHandling:error.code];
                NSLog(@"%@",error.userInfo);
            }
        }];
    };
    
}

-(IBAction)onSignIn:(id)sender{
    
    [PFUser logInWithUsernameInBackground:signInUsername.text password:signInPass.text
                                    block:^(PFUser *user, NSError *error) {
                                        if (user) {
                                            [self successful];
                                        } else {
                                            [self errorHandling:error.code];
                                            NSLog(@"%@",error.userInfo);
                                        }
                                    }];
}

-(IBAction)onReset:(id)sender{
    
    [PFUser requestPasswordResetForEmailInBackground:resetPassEmail.text];
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    MainViewController *viewController = (MainViewController *)[storyboard instantiateViewControllerWithIdentifier:@"signInView"];
    [self presentViewController:viewController animated:YES completion:nil];
}

- (void) errorHandling:(long)code{
    if (code == 125) {
        [self alertMethod:@"Invalid Email Address" message:@"Please enter a valid email"];
    }else if (code == 203){
        [self alertMethod:@"Email is already taken" message:@"Please enter a different one"];
    }else if (code == 202){
        [self alertMethod:@"Username is already taken" message:@"Please enter a different one"];
    }else if (code == 101){
        [self alertMethod:@"Username or Password is incorrect" message:@"Try again or reset your password"];
    }
}

- (void) successful{
    
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    MainViewController *viewController = (MainViewController *)[storyboard instantiateViewControllerWithIdentifier:@"mainNav"];
    [self presentViewController:viewController animated:YES completion:nil];
    //[self.navigationController pushViewController:viewController animated:YES];

}

- (void) alertMethod:(NSString*) mtitle message:(NSString*) message{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:mtitle
                                                    message:message
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
}

//---------- Hiding Keyboard when touching anything but the field----------------------

- (void) touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    
    [signInPass resignFirstResponder];
    [signInUsername resignFirstResponder];
    [signUpPass resignFirstResponder];
    [signUpUsername resignFirstResponder];
    [signUpEmail resignFirstResponder];

    
}
//-------------------------------------------------------------------------------------


- (BOOL) returnKey:(UITextField*)textField{
    if (textField) {
        [textField resignFirstResponder];
    }
    return NO;
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
