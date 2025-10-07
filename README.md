Text Animator is an extension of the text renderer. You can use tags to add animation to some words almost anywhere.

## Demo (with Blabber and Heracles mod)

```
<typewriter><wiggle>TextAnimator</wiggle> is a mod by <wave><rainb>Snownee</rainb></wave>.
```

![demo](https://cdn.modrinth.com/data/HBIG5nRf/images/e15e996bb6b5311bc0c0ae9e71d42a9d07ba9911.webp)

## Effects

![demo](https://cdn.modrinth.com/data/HBIG5nRf/images/dcfca909c5d9c43d2817bf18f18d533d6e464083.webp)

### Basic usage

* Add effect: `<effectName param1=value1 param2=value2 param3>text`
* Remove effect: `</effectName>text`
* Effects can be **stacked**.

### typewriter

Can only be used at the beginning of the text. Rendering characters or words one by one, simulating typing.

- Example: `<typewriter>Typewriter Effect`

### bounce

Make characters bounce back vertically.

- Parameters:
    - `a`: Amplitude (default `1.0`)
    - `f`: Frequency (default `1.0`)
    - `w`: Wave Size (default `1.0`)
- Example: `<bounce a=4 f=1.8 w=0.2>BOING!`

### fade

Periodically change opacity (breathing light).

- Parameters:
    - `a`: Minimum Opacity (default `0.3`)
    - `f`: Frequency (default `1.0`)
    - `w`: Wave Size (default `0.0`)
- Example: `<fade a=0.2 f=1.5 w=0.1>Fading Text`

### glitch

Create text slicing, and occasional flicker, generating additional layers in some cases.

- Parameters:
    - `f`: Frequency (default `1.0`)
    - `j`: Character Jitter Chance (default `0.015`)
    - `b`: Character Blink Chance (default `0.003`)
    - `s`: Slicing/Shifting Chance (default `0.08`)
- Example: `<glitch f=3 j=0.02 b=0.01 s=0.1>ERROR`

### grad

Support linear gradient in RGB or HSV, can be flowing and segmented.

- Parameters:
    - `from`: Start Color (default `5BCEFA`)
    - `to`: End Color (default `F5A9B8`)
    - `hue`: Whether to interpolate in HSV space (default `false`)
    - `f`: Flow Speed (`0` for static, default `0.0`)
    - `sp`: Span, affecting color distribution between characters (default `20.0`)
    - `uni`: Whether to be unidirectional without swinging (default `false` for back-and-forth)
- Example: `<grad from=#7FFFD4 to=#1E90FF hue uni>Flowing Gradient Text`

### neon

Draw multiple glowing outlines for characters.

- Parameters:
    - `p`: Sampling Count, at least `4` (default `10`)
    - `r`: Radius (default `2`)
    - `a`: Opacity Multiplier (default `0.12`)
- Example: `<neon p=8 r=2 a=0.15>Neon Glow`

### pend (Pendulum/circular)

Characters swing around the top-middle, can plus small circular trajectories.

- Parameters:
    - `f`: Frequency (default `1.0`)
    - `a`: Maximum Angle in degrees (default `30.0`)
    - `r`: Circular Radius (default `0.0`)
- Example: `<pend f=1.0 a=30 r=2>Swinging Around`

### pulse

Overall brightness fluctuates over time (does not affect shadow layer).

- Parameters:
    - `base`: Minimum Brightness Multiplier (default `0.75`)
    - `a`: Amplitude (default `1.0`)
    - `f`: Frequency (default `1.0`)
    - `w`: Wave Size (default `0.0`)
- Example: `<pulse base=0.6 a=0.4 f=1.5>Power Rising`

### rainb (Rainbow)

Cycle colors over HSV.

- Parameters:
    - `f`: Frequency (default `1.0`)
    - `w`: Wave Size (default `1.0`)
- Example: `<rainb f=1 w=0.5>Colorful Text!`

### shadow

Modify the offset and color of the text shadow.

- Parameters:
    - `x`: Horizontal Offset Delta (default `0.0`)
    - `y`: Vertical Offset Delta (default `0.0`)
    - `c`: Color in hex (e.g. `FF0000` for red) (default `000000`)
    - `r`,`g`,`b`: Color (default `0.0`)
    - `a`: Opacity (default `1.0`)
- Example: `<shadow a=0>No Shadow</shadow> <shadow r=1 a=0.6>Shadowed Text`

### shake

Randomly jitter in all directions, creating a sense of tension/energy.

- Parameters:
    - `a`: Amplitude (default `1.0`)
    - `f`: Frequency (default `1.0`)
- Example: `<shake a=2 f=3>WARNING!`

### swing

Characters spinning back and forth.

- Parameters:
    - `a`: Amplitude (default `1.0`)
    - `f`: Frequency (default `1.0`)
    - `w`: Wave Size (default `0.0`)
- Example: `<swing a=0.5 f=1.8>Waving Text`

### turb (Turbulence)

Random displacement perturbation based on noise.

- Parameters:
    - `a`: Amplitude (default `1.0`)
    - `f`: Frequency (default `1.0`)
- Example: `<turb a=2 f=1.5>Windy Text`

### wave

Characters undulating up and down like waves.

- Parameters:
    - `a`: Amplitude (default `1.0`)
    - `f`: Frequency (default `1.0`)
    - `w`: Wave Size (default `1.0`)
- Example: `<wave a=1 f=1.0 w=0.5>Flowing Text`

### wiggle

Each character wiggles along a fixed random direction.

- Parameters:
    - `a`: Amplitude (default `1.0`)
    - `f`: Frequency (default `1.0`)
    - `w`: Wave Size (default `1.0`)
- Example: `<wiggle a=1 f=2>Wiggly Text!`

### Parameter Quick Reference

- `a`: Amplitude/Opacity (meaning varies slightly across effects)
- `f`: Speed/Frequency
- `w`: Wave Size
- `r`: Radius

### Notes

- Multi-layer rendering: Effects like `glitch` and `neon` will generate additional layers or slices.
- Rendering Order: Effects added later execute first, potentially altering inputs (e.g., coordinates/color/opacity) of preceding effects.
- Performance: High `p` (neon sampling) and complex overlays increase overhead; use judiciously.

## Configuration

Options like typewriter speed and mode can be found in Chat Settings.

## Known incompatible mods

- Modern UI
- Caxton