//
//  ViewController.m
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import "MainViewController.h"

@interface MainViewController ()

@end

@implementation MainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    PFUser *currentUser = [PFUser currentUser];
    if (currentUser) {
        
    } else {
        NSUserDefaults *userDefaults = [[NSUserDefaults alloc] init];
        if ([userDefaults boolForKey:@"yourKey"]) {
            [self notLoggedIn:@"signUpView"];
        }else{
            [self notLoggedIn:@"signInView"];
        }
    }
}

-(IBAction)onClick:(UIButton*)sender{
    if (sender.tag == 0) {
        [PFUser logOut];
        [self notLoggedIn:@"signInView"];
    }
}

-(void) notLoggedIn:(NSString*) identifier{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    MainViewController *viewController = (MainViewController *)[storyboard instantiateViewControllerWithIdentifier:identifier];
    [self presentViewController:viewController animated:YES completion:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
