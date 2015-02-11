//
//  ViewController.m
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import "MainViewController.h"
#import "TaskTableViewCell.h"

@interface MainViewController ()

@end

@implementation MainViewController
@synthesize tasksArray;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    currentCell = [[Tasks alloc] init];
    
    PFUser *currentUser = [PFUser currentUser];
    if (currentUser) {
        
    } else {
        NSUserDefaults *userDefaults = [[NSUserDefaults alloc] init];
        if ([userDefaults boolForKey:@"signedUp"]) {
            [self notLoggedIn:@"signUpView"];
        }else{
            [self notLoggedIn:@"signInView"];
        }
    }
    [self retriveTasks];
}


- (void) retriveTasks{
    
    PFQuery *query = [PFQuery queryWithClassName:@"Tasks"];
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            // The find succeeded.
            tasksArray = objects;
            NSLog(@"Successfully retrieved %lu scores.", (unsigned long)objects.count);
        } else {
            // Log details of the failure
            NSLog(@"Error: %@ %@", error, [error userInfo]);
        }
    }];
}

#pragma TableView Functions

/*- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
 
 return 70;
 }*/

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return [tasksArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    TaskTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    currentCell = [tasksArray objectAtIndex:indexPath.row];
    [cell refreshCellWithInfo:currentCell.title priority:currentCell.priority];
    
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}


-(IBAction)onClick:(UIButton*)sender{
    if (sender.tag == 0) {
        //[PFUser logOut];
        //[self notLoggedIn:@"signInView"];
        [taskTableView reloadData];

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
