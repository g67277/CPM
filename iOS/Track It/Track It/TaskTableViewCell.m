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
    taskName.font = [UIFont fontWithName:@"GoodTimesRg-Regular" size:15];
    
    taskPriority.text = [self showPriority:incomingPriority];
    taskPriority.textColor = [UIColor darkGrayColor];
    taskPriority.font = [UIFont fontWithName:@"GoodTimesRg-Regular" size:15];
 
}

- (NSString*) showPriority: (int) p{
    
    switch (p) {
        case 1:
            return @"!";
            break;
        case 2:
            return @"!!";
            break;
        case 3:
            return @"!!!";
            break;
        case 4:
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
