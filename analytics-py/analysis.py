import pandas as pd

df = pd.read_csv("../data/raw/account_data.csv")
print("CSV loaded successfully. Columns: ", df.columns)


currency_columns = ["Account Balance", "Debit Amount", "Credit Amount"]
for col in currency_columns:
    clean_col = f"{col}_Clean"
    df[clean_col] = (
        df[col]
        .astype(str)
        .str.replace("$", "", regex=False)
        .str.replace(",", "", regex=False)
        .replace(["", "nan", "None"], pd.NA)
        .astype(float)
    )

print("Currency column cleaned successfully: ", df.columns)
print(df[[f"{currency_columns[0]}_Clean"]].head())


date_column = "Transaction Date"
df[date_column] = pd.to_datetime(
    df[date_column],
    errors="coerce"   # invalid or blank dates → NULL
)
print("Date column parsed successfully.")
print(df[[date_column]].head())


