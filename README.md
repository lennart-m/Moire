This is a simple JavaFX app that simulates the Moiré patterns from Numberphile's "Freaky dot patterns" video, see https://www.youtube.com/watch?v=QAja2jp1VjE.

Two identical images are shown. The bottom one is fixed in position, the other one lies above it. The above image may be transformed in order to create a Moiré pattern. The transformation applied is affine and so its rotation, translation (x, y) and scaling (common or x, y separately) can be changed.

In the "image setup" section the image may be customized, for example the image may contain random pixels or a regular pattern. The foreground and background color of the image can be changed.

Mouse inputs on the Moiré image are handled and so the transformation may be changed in an intuitive way. This includes:
- **moving**: holding the left mouse button and moving the mouse results in changing the translation values
- **rotating**: holding the right mouse button and moving the mouse changes the rotation value
- **scaling**: scrolling the mouse wheel results in a common scaling of the x and y directions
- **scaling x and y differently**: holding the middle mouse button and moving the mouse does change the x and y scaling values

![Program screenshot](https://dl.dropboxusercontent.com/u/18980656/Moiree/Screenshot-v2.0.png "Program screenshot")
