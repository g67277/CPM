//
//  TaskTableViewCell.m
//  Track It
//
//  Created by Nazir Shuqair on 2/10/15.
//  Copyright (c) 2015 Me Time Studios. All rights reserved.
//

#import "TaskTableViewCell.h"

@implementation TaskTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void) refreshCellWithInfo: (NSString*) incomingTask priority:(int) incomingPriority{
    
    taskName.text = incomingTask;
    taskName.textColor = [UIColor darkGrayColor];
    taskName.font = [UIFont fontWithName:@"GoodTimesRg-Regular" size:18];
    
    taskPriority.text = [self showPriority:incomingPriority];
    taskPriority.textColor = [UIColor whiteColor];
    taskPriority.font = [UIFont fontWithName:@"GoodTimesRg-Regular" size:18];
 
}

- (NSString*) showPriority: (int) p{
    
    switch (p) {
        case 1:
            taskPriority.backgroundColor = [UIColor colorWithRed:51.0/255.0f
                                                           green:238.0/255.0f
                                                            blue:136.0/255.0f alpha:1];
            return @"!";
            break;
        case 2:
            taskPriority.backgroundColor = [UIColor colorWithRed:51.0/255.0f
                                                           green:238.0/255.0f
                                                            blue:221.0/255.0f alpha:1];
            return @"!!";
            break;
        case 3:
            taskPriority.backgroundColor = [UIColor colorWithRed:238.0/255.0f
                                                           green:51.0/255.0f
                                                            blue:68.0/255.0f alpha:1];
            return @"!!!";
            break;
        case 4:
            taskPriority.backgroundColor = [UIColor colorWithRed:41.0/255.0f
                                                           green:64.0/255.0f
                                                            blue:106.0/255.0f alpha:1];
            return @"!!!!";
            break;
        default:
            break;
    }
    return @"";
}

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
