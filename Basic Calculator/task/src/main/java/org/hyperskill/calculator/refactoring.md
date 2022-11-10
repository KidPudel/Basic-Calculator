# Refactoring

**We need to separate View from ViewModel**

**there is a better way to get a view instance**
FindViewById is not good
so use ViewBinding instead and inflate it instantiate it 

**we need to find what is repetitive**
setOnClick listener is a lot
so create a method for it

## create a set on click listener method

right now we are writing logic for every button separately, **Thats reeaaly bad**
so separate it too

## create a method to reduce repetitive logic for the button
for example we can create a method digitClicked or smth like this
and the same of for operational buttons