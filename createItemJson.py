import sys
import json

name = sys.argv[1]


data = {
  "parent": "builtin/generated",
  "textures": {
    "layer0": "hydcraft:items/" + name
  },
  "display": {
    "thirdperson": {
      "rotation": [ -90, 0, 0 ],
      "translation": [ 0, 1, -3 ],
      "scale": [ 0.55, 0.55, 0.55 ]
    },
    "firstperson": {
      "rotation": [ 0, -135, 25 ],
      "translation": [ 0, 4, 2 ],
      "scale": [ 1.7, 1.7, 1.7 ]
    }
  }
}

print(data)
with open('resources/assets/hydcraft/models/item/' + name + ".json", 'w') as outfile:
    json.dump(data, outfile)

