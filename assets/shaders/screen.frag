#version 120

const float width = 1440.0, height = 800.0;
uniform float radius = 1.3, falloff = 3.0, tint = .4;
uniform float characterY = height / 2.0;
uniform float characterX = width / 2.0;

void main() {
    // shader will follow the character based on the y coordinate
    vec2 center = vec2(characterX, characterY);

    // vector distance from the center of the screen to the current (x, y)
    float distance = length(gl_FragCoord.xy - center);

    // normalize the distance so when radius = 1.0, circle diameter is equal to screen height
    distance /= (height / 2.0);

    // smoothly change visibility based on distance
    float visibility = smoothstep(radius, radius + falloff, distance) + tint;

    // set the fragment color to black, the only thing that varies is the alpha/visibility
    gl_FragColor = vec4(0.0, 0.0, 0.0, visibility);
}
