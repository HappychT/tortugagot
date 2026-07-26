const fs = require('fs');

const overridesContent = fs.readFileSync('data/overrides.js', 'utf-8');
const territoriesContent = fs.readFileSync('data/territories.js', 'utf-8');

// Execute JS to get objects
const evalOverrides = new Function(`${overridesContent}; return OVERRIDES;`);
const evalTerritories = new Function(`${territoriesContent}; return typeof TERRITORIAL_DATA !== 'undefined' ? TERRITORIAL_DATA : [];`);

const overrides = evalOverrides();
const territories = evalTerritories();

fs.writeFileSync('scratch/overrides.json', JSON.stringify(overrides, null, 2));
fs.writeFileSync('scratch/territories.json', JSON.stringify(territories, null, 2));
console.log('Successfully dumped JS data to JSON!');
