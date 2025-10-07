# TextAnimator 使用文档

> 语法示例：
> 添加效果：`<effectName param1=value1 param2=value2 param3>文字`
> 取消效果：`</effectName>文字`
> 效果可以**叠加使用**，例如：
>
> ```text
> <typewriter><wiggle>TextAnimator</wiggle> is a mod by <wave><rainb>Snownee</rainb></wave>。
> ```
> ![demo](assets/typewriter.webp)

---

## 效果列表

### 1. bounce（弹跳）
让字符在竖直方向“落地回弹”。
- 参数：`a`（幅度，默认 `1.0`）、`f`（速度，默认 `1.0`）、`w`（相位，默认 `1.0`）
- 示例：`<bounce a=4 f=1.8 w=0.2>BOING!</bounce>`

### 2. fade（透明呼吸）
周期性改变透明度（呼吸灯）。
- 参数：`a`（最小透明度，默认 `0.3`）、`f`（速度，默认 `1.0`）、`w`（相位，默认 `0.0`）
- 示例：`<fade a=0.2 f=1.5 w=0.1>Fading Text</fade>`

### 3. glitch（电子故障）
制造屏幕抖动、切片与偶发闪烁，部分情况下产生分层渲染。
- 参数：`intensity`（强度，默认 `1.0`）、`f`（频率，默认 `2.5`）、`shift`（偏色几率，默认 `0.04`）、`flicker`（闪烁几率，默认 `0.002`）
- 示例：`<glitch intensity=1.2 f=3 shift=0.05 flicker=0.01>ERROR_404</glitch>`

### 4. grad（渐变）
支持 RGB 或 HSV 的线性渐变，可流动与分段。
- 参数：
    - `from`（起始色，默认 `5BCEFA`）
    - `to`（结束色，默认 `F5A9B8`）
    - `hue`（是否在 HSV 空间插值，默认 `false`）
    - `f`（流动速度，`0` 为静态，默认 `0.0`）
    - `sp`（跨度，影响字符间颜色分布，默认 `20.0`）
    - `uni`（是否单向不回摆；`true` 单向，默认 `false` 即来回循环）
- 示例：
```text
<grad from="#7FFFD4" to="#1E90FF" hue=true f=0.3 sp=30>
  Flowing Gradient Text
</grad>
```

### 5. neon（霓虹模糊）
为字符绘制多次模糊的发光描边。
- 参数：`p`（采样次数，至少 `4`，默认 `10`）、`r`（半径，默认 `2`）、`a`（透明度倍乘，默认 `0.12`）
- 示例：`<neon p=8 r=2 a=0.15>Neon Glow</neon>`

### 6. pend（钟摆/圆周）
字符围绕中心摆动，可叠加小圆周轨迹。
- 参数：`f`（速度，默认 `1.0`）、`maxAngle`（最大角度°，默认 `30.0`）、`radius`（圆周半径，默认 `0.0`）
- 示例：
```text
<pend f=1.0 maxAngle=30 radius=2>
  Swinging Around
</pend>
```

### 7. pulse（亮度脉动）
整体亮度随时间波动（不影响阴影层）。
- 参数：`base`（最小亮度倍率，默认 `0.75`）、`a`（幅度，默认 `1.0`）、`f`（速度，默认 `1.0`）、`w`（相位，默认 `0.0`）
- 示例：`<pulse base=0.6 a=0.4 f=1.5>Power Rising</pulse>`

### 8. rainb（彩虹）
在 RGB 上循环变色（不影响阴影层）。
- 参数：无
- 示例：`<rainb>Colorful Text!</rainb>`

### 9. scroll（水平滚动）
文本持续向水平方向移动。
- 参数：`f`（速度，默认 `1.0`；为负时向右滚动）
- 示例：`<scroll f=0.8>News Ticker →</scroll>`

### 10. shadow（阴影着色/偏移）
仅对“阴影层”生效：改变阴影的偏移与颜色。
- 参数：`x`（水平偏移，默认 `1.0`）、`y`（垂直偏移，默认 `1.0`）、`r,g,b,a`（颜色与透明度，默认 `0,0,0,1.0`）
- 示例：`<shadow x=2 y=2 r=0 g=0 b=0 a=0.6>Shadowed Text</shadow>`

### 11. shake（随机细抖）
在各方向随机抖动，营造紧张/能量感。
- 参数：无
- 示例：`<shake>WARNING!</shake>`

### 12. swing（水平摆动）
字符在水平轴轻微摇摆（旋转）。
- 参数：`a`（幅度，默认 `1.0`）、`f`（速度，默认 `1.0`）、`w`（相位，默认 `0.0`）
- 示例：`<swing a=3 f=1.8>Waving Text</swing>`

### 13. turb（乱流）
基于噪声的随机位移扰动。
- 参数：`a`（幅度，默认 `1.0`）、`f`（速度，默认 `1.0`）
- 示例：`<turb a=2 f=1.5>Windy Text</turb>`

### 14. wave（波浪）
字符上下起伏的水波效果。
- 参数：无
- 示例：`<wave>Flowing Text</wave>`

### 15. wiggle（定向抖摆）
每个字符沿固定随机方向轻摆。
- 参数：无
- 示例：`<wiggle>Wiggly Text!</wiggle>`

### 16. typewriter（打字机）
逐字显示文本；与其他效果叠加时，渲染会逐字符推进。
- 参数：无
- 建议：将 `<typewriter>` 放在段落起始处。
- 示例：
```text
<typewriter>
  <bounce a=2 f=1.0>
    Hello, TextAnimator!
  </bounce>
</typewriter>
```

---

## 注意事项
- 多层渲染：`glitch`、`neon` 等效果可能产生额外层或切片。
- 阴影层：`shadow` 只影响阴影；`pulse`/`rainb`/`grad` 不会在阴影层改变颜色。
- 叠加顺序：后添加的效果先执行，可能改变前一个效果的输入（如坐标/颜色/透明度）。
- 性能：高 `p`（neon 采样）与复杂叠加会增加开销，按需使用。

## 参数速查
- `a`：幅度/透明度等（不同效果含义略有差异）
- `f`：速度/频率
- `w`：相位偏移
- `p`：采样次数（neon）
- `r`：半径（neon）
- `from` / `to`：颜色（hex 字符串，可带 `#`）
- `hue`：是否在 HSV 空间插值（grad）
- `sp`：渐变跨度（grad）
- `uni`：单向渐变（`true` 为单向；默认循环）
- `base`：基础亮度（pulse）
- `maxAngle` / `radius`：摆动角度与圆周半径（pend）
- `intensity` / `shift` / `flicker`：强度、偏色与闪烁几率（glitch）
