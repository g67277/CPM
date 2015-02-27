//
//  ViewController.m
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import "MainViewController.h"
#import "TaskTableViewCell.h"
#import "AddEditViewController.h"
#import "SignUpInViewController.h"
#import "Reachability.h"

@interface MainViewController (){
}

@end

@implementation MainViewController

-(void) viewWillAppear:(BOOL)animated{
    
    self.navigationItem.title = @"Track It";
    [self retriveTasks];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reachabilityDidChange) name:kReachabilityChangedNotification object:nil];
    
    [self reachabilityDidChange];
    
    currentCell = [[Tasks alloc] init];
    taskArray = [[NSMutableArray alloc] init];
    taskTableView.allowsMultipleSelectionDuringEditing = NO;
    logins = [[SignUpInViewController alloc] init];
    
    PFUser *currentUser = [PFUser currentUser];
    if (currentUser) {
        
    } else {
        NSUserDefaults *userDefaults = [[NSUserDefaults alloc] init];
        if ([userDefaults boolForKey:@"signedUp"]) {
            [self notLoggedIn:@"signInView"];
        }else{
            [self notLoggedIn:@"signUpView"];
        }
    }
    
    if (isOnline) {
        [NSTimer scheduledTimerWithTimeInterval:20.0 target:self selector:@selector(retriveTasks) userInfo:nil repeats:YES];
    }
}


- (void) retriveTasks{
    
    if (isOnline) {
        PFQuery *query = [PFQuery queryWithClassName:@"Tasks"];
        [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (!error) {
                // The find succeeded.
                
                [taskArray removeAllObjects];
                for (int i = 0; i < objects.count; i++) {
                    PFObject* currentObject = objects[i];
                    Tasks *task = [[Tasks alloc]init];
                    task.title = currentObject[@"title"];
                    task.tdescription = currentObject[@"description"];
                    task.priority = [[currentObject objectForKey:@"priority"] intValue];
                    task.objectID = currentObject.objectId;
                    [taskArray addObject:task];
                }
                [taskTableView reloadData];
            } else {
                // Log details of the failure
                NSLog(@"Error: %@ %@", error, [error userInfo]);
            }
        }];
    }
    
}

#pragma TableView Functions

/*- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
 
 return 70;
 }*/

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return [taskArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    TaskTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    currentCell = [taskArray objectAtIndex:indexPath.row];
    [cell refreshCellWithInfo:currentCell.title priority:currentCell.priority];
    
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        
        currentCell = [taskArray objectAtIndex:indexPath.row];

        PFQuery *query = [PFQuery queryWithClassName:@"Tasks"];
        
        // Retrieve the object by id
        [query getObjectInBackgroundWithId:currentCell.objectID block:^(PFObject *taskToDelete, NSError *error) {
            
            [taskToDelete deleteInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                if (succeeded) {
                    [taskArray removeObject:currentCell];
                    [taskTableView reloadData];
                }else{
                    NSLog(@"Error: %@ %@", error, [error userInfo]);
                }
            }];
        }];
    }
}


#pragma Segue

//------------------------------------- Segue methode -------------------------------------------------------------

- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    
    if ([segue.identifier  isEqual: @"toDetail"]) {
        AddEditViewController* detailView = segue.destinationViewController;
        if (detailView != nil) {
            UITableViewCell *cell = (UITableViewCell*)sender;
            NSIndexPath *indexPath = [taskTableView indexPathForCell:cell];
            
            // get the string from the array based on the cell in the tabel view we clicked
            
            Tasks *selectedObject = [taskArray objectAtIndex:indexPath.row];
            
            detailView.currentCell = selectedObject;
        }
    }
}


-(IBAction)onClick:(UIButton*)sender{
    if (sender.tag == 0) {
        [PFUser logOut];
        [self notLoggedIn:@"signInView"];
    }else if(sender.tag == 2){
        if (isOnline) {
            [self retriveTasks];
        }else{
            [self alertMethod:@"Offline" message:@"Please make sure you're connected and try again"];
        }
    }
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    if ([identifier isEqualToString:@"toAdd"]) {
        
        if (isOnline) {
            return YES;
        }else{
            [self alertMethod:@"Offline" message:@"Please make sure you're connected and try again"];
            return NO;
        }
    }
    
    return YES;
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

- (void) alertMethod:(NSString*) mtitle message:(NSString*) message{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:mtitle
                                                    message:message
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
}

//-------------------------------------------Network monitoring--------------------------------------------

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
@end
