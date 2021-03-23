
Russel: i don't think so...90% of 50% is 45%...45% to left and 45% to right...result is capture the "middle" 90%....

Russel: context was always "left to right" hence 1-p is lowest 10 % left of mean, p referred to first 90% above the mean

Russel: yes, but wobulation prob somewhat misleading now that i think about it...more of the language used in that doc yesterday is prob better, i.e., converge to a steady state solution over time...(wobulating in the process, but ultimate objective is "best possible characterization based on all data processed up to any given point in time")...i'll answer that latest question in a sec

Brad: can you ask him what the "mean" of the "simple validation case" should be so I can calibrate my code's answer with his?

Russel: beginning:  1.061111

Russel: 0.897205 after 125

Brad: is the bin that contains the mean part of the (1-p) or (p) (i.e., is it considered to be above or below? neither? both?

Russel: Interpolate(linear)...some in lower half some in upper half...use decimals, not discrete 

Russel: So if mean was 1.04, in bin 1-1.1, Lower contribution (1.04-1)/.1*n; Upper (1.1-1.04)/.1*n :: .1 since that's bin width, n is the bin cnt

Brad: okay, tired of looking at this... I've got initial mean/min/max and final mean/min working; final max coming back as 1.1 instead of 1.0

Brad: probably a "clopen" thing

Brad: or a <= vs < int he case of choosing whether we hit p or not

Martha: Yeah ...he did say he was hoping his numbers were right ...ugh

Russel: when I first did it I was  using a small bin assumption and dividing the count for that bin halh & half, i.e., half below and half above...then i figured you guys could easily handle the interpolation so added that to the req...then later i updated my spreadsheet to also interpolate and probably didn't notice the change in bin ranges...didn't expect it to be so different...might be something to consider as a test case (compare both ways).  This, however is pretty contrived data so  prob shouldn't read too much into it

if possible, ask brad to put in a switch so that can run it to interpolate mean within the bin or just assumeit to be the midpoint.

Russel: actually I mean to interpolate how many of the values are left/right of the mean, or assume it's half and half
