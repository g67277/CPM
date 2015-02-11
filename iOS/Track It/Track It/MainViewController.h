//
//  ViewController.h
//  Track It
//
//  Created by Nazir Shuqair on 2/8/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "Tasks.h"

@interface MainViewController : UIViewController{
    
    IBOutlet UITableView* taskTableView;
    
    Tasks *currentCell;
    
}

@property (nonatomic, assign) NSArray* tasksArray;


-(IBAction)onClick:(id)sender;

@end

