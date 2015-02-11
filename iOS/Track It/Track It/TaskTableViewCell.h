//
//  TaskTableViewCell.h
//  Track It
//
//  Created by Nazir Shuqair on 2/10/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TaskTableViewCell : UITableViewCell{
    
    IBOutlet UILabel* taskName;
    IBOutlet UILabel* taskPriority;
    
}

- (void) refreshCellWithInfo: (NSString*) incomingTask priority: (int) incomingPriority;


@end
