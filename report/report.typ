// Metadata for the PDF, not strictly neccecary but it's nice
#set document(
    title: "Group 14s report for Agile software development, fall of 2024", 
    author: ("Nikhil", "Ali", "Daniell", "Gunnar", "Jesper", "Oscar", "Markus", "Suheib Shahin", "Ida", "Yousef"),
    date: auto,
)

// Paragraph styling
#set par(
    justify: true,
    leading: 0.5em,
)

// setting the font and text size
#set text(
    font: "Linux Libertine",
    size: 12pt,
)

// Report starts here
// The titile, aligned to the center
#align(center,[
    = THIS IS ONLY A ROUGH DRAFT TO GET ALL THOUGHTS WRITTEN DOWN. IT SHOULD UNDER NO CIRCUMSTANCE BE SUBMITTED(TODO: change title)\
])
\

This report aims to show how the project of group 14, aka "The real housewives of agile", has progressed and what hardships were encountered and overcome. It will touch on the product side of things as well as the human side.

== The Product

TODO: Write about the product, should wait until later in the sprint
// The product. Compare the end result with what you described in the project scope.
//   * Did you end up with a product close to the initial thoughts you had? Why or why not did this happen? Was your first mockup somewhat accurate? 
//   * Consider all sprints, what are the three most important learnings (what did you learn and how did you adapt to it) you discovered along the way 
//       about the product you built? It could be about the usage, the users, the technology behind, the features included etc.

There are four features we ended up dropping from our original plan: the ability to search or filter for user-made guides, the ability to add images to user made guides, the ability to leave comments on a guide, and the ability to edit published posts.
Although these are useful features that we would like to have implemented, they are not essential to the functionality of the site. We ended up dropping them due to their lower priority and the time and effort needed to implement them outweighed the impact and benefits they would have on the product.
This highlights one of the strengths of the agile working process, namely by making the desired and required features different stories in our plan we are able to remain more flexible and always focus on the highest priority tasks without compromising any of the core functionality of the product.
If we had used a more classical approach (say waterfall for example) we might have spent too much time on these features with the risk of not having a finished (or working!) product by the final deadline.

Dropping the ability to leave comments on the guides did lead to a change away from our initial visual design sketch.//ref to image
Another change we did make was that instead of having the rating at the bottom of the page, we decided on presenting the difficulty rating closer to the top of the page. Right underneath the title of the guide.
Apart from this our final product has followed the visual design sketch as planned. 

TODO: write about the teamwork and challenges faced. \

The greatest, or most persistent, issue we faced was that the team was too large to effectively coordinate. At first we managed well, as the team quickly separated into two groups. One working on the website and one on the backend. This approach worked well as this meant two smaller teams that were easier to coordinate could work on the project in parallel. Thus eliminating the issue of having such a large team. It also aligns with the agile work process as this came naturally and was self organised.\
This however did not last forever as eventually the website and backend were merged together to make one coherent product. At this point cooperating and coordinating became significantly harder and finding work for every member equally was not simple. During these later sprints the result was some members doing more work or being more active while others did less work.\
The solution used was to make sure every member did something small, so that each member still could contribute. One example could be doing a small bugfix or researching some information for future sprints.\
\
Another issue we faced was the lack of experience within the team. Since the team consisted of mostly inexperienced students a significant portion of time was allocated to researching tools and figuring out the best way to do things. In the end this was solved by deciding not to use tools that were deemed impractical to learn such as the react framework and using tools that were already familiar such as java and SQL as well as pure HTML, CSS and JavaScript.\
\
Of these two major issues the second could be considered more or less fully dealt with however the first only partially. This is the nature of such projects and not much more could be done about this without more rigorous planning and experience.\
#line(length:100%)
The idea of daily scrums was something we didn't do much at all. We instead opted for two meeting per sprint (week) to allow for each meeting to be more informative and give more time to work for each team member.\
As mentioned before, the team initially self organised into two groups where each member felt the most comfortable. These were the database team and the website team. This was a result of what each team member was most comfortable with.\
The initial idea was to not work weekends however as the project progressed and the schedules of the team members clashed working weekends became a reasonable option and common place. This was not an issue but shows how time management can be very difficult with such large teams.\
\
The team also did not start out with a unified style however after consulting with the TA and internal discussion a unified style was decided on and followed. This was a small but unnecessary chunk of time wasted on refactoring code to follow the enw standard and should have been discussed earlier, however this is only a small issue and the refactoring was quickly taken care of.\
\
Another thing that was not planned enough was the social contract. It should have had a clause for what was expected of someone if they were away sick. Some members did get sick during the sprints and a more rigorous and transparent idea of what was expected of them would have aligned better with the principles of agile.
