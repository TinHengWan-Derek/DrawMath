DrawMath by Tin Heng Wan (Derek) using Java

Intention:

    A handwriting drawing application that specifically used to write out
    all the step involved in solving some simple Math problem.

Problem:

    Most of the handwriting drawing application in the app store use lots
    of the screen space to show irrelevant step. For example,
    ```
    (0)   1*2*3*4
    (1) = 2*3*4
    (2) = 6*4
    (3) = 24
    ```
    To calculate (2), we can just display out (1) instead of (1) and (0)


Solution:

    Since every step in the Math problem solving process only need one or two
    previous step, only display those key step will largely reduce the use of
    screen space and show those key steps in detail.

Implementation/Usage:

    The overall screen will split into two section.

    The section on the top will display the previous step.
    The section on the bottom will be the current drawing section.

    Press [Enter] to draw next step.

    Press [Shift + Enter] save all the step to an image.

    Since the initial problem might be refer sometime, press 1 will show the problem.

    There are some other feature in the DrawMath.java, however there are some bug with them.
    Which I might fix or delete later.
